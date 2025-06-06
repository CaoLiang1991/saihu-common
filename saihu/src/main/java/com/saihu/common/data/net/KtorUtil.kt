package com.saihu.common.data.net

import com.google.gson.Gson
import com.saihu.common.data.model.HttpResponse
import com.saihu.common.util.SpUtil
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpCallValidator
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.plugin
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.InternalAPI
import io.ktor.utils.io.readUTF8Line
import kotlinx.serialization.json.Json
import java.util.Date

object KtorUtil {
    lateinit var httpClient: HttpClient

    fun init(hostUrl: String) {
        httpClient = HttpClient(OkHttp) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                    isLenient = true
                })
            }
            install(HttpTimeout) {
                requestTimeoutMillis = 20 * 1000
            }
            install(Logging) {
                logger = Logger.DEFAULT // 使用默认控制台输出
                level = LogLevel.ALL    // 日志级别: ALL, HEADERS, BODY, INFO, NONE
            }
            defaultRequest {
                url(hostUrl)
                contentType(ContentType.Application.Json)
                SpUtil.getAccount()?.let { account ->
                    headers["Authorization"] = "Bearer ${account.token.token}"
                }
            }
            expectSuccess = true
            HttpResponseValidator(httpResponseValidatorConfig)
        }
        httpClient.plugin(HttpSend).intercept { request ->
            val originalCall = execute(request)
            if (!request.url.toString().contains("login") && !request.url.toString()
                    .contains("refreshtoken")
            ) {
                var account = SpUtil.getAccount()!!
                var token = account.token
                val diff = token.tokenExpires - (Date().time / 1000)
                if (originalCall.response.status.value == 401 || (diff > 0 && diff <= 60 * 60)) {
                    account = post("auth/refreshtoken") {
                        setBody(
                            mapOf("refreshToken" to account.token.refreshToken)
                        )
                    }!!
                    SpUtil.saveAccount(account)
                    request.headers["Authorization"] = "Bearer ${token.token}"
                    execute(request)
                } else {
                    originalCall
                }
            } else {
                originalCall
            }
        }
    }

    suspend inline fun <reified T> get(
        urlString: String,
        block: HttpRequestBuilder.() -> Unit = {}
    ): T? {
        return httpClient.get(urlString, block).body<HttpResponse<T?>>().data
    }

    suspend inline fun <reified T> post(
        urlString: String,
        block: HttpRequestBuilder.() -> Unit = {}
    ): T? {
        return httpClient.post(urlString, block).body<HttpResponse<T?>>().data
    }
}

@OptIn(InternalAPI::class)
private val httpResponseValidatorConfig: HttpCallValidator.Config.() -> Unit = {
    val gson = Gson()

    validateResponse { response ->
        if (response.status != HttpStatusCode.OK) {
            val data =
                gson.fromJson(response.content.readUTF8Line(), HttpResponse::class.java)
            throw Exception("错误码: ${data.code}, 错误信息: ${data.msg}")
        }
    }
    handleResponseExceptionWithRequest { exception, _ ->
        val clientException = exception as? ClientRequestException
            ?: return@handleResponseExceptionWithRequest
        val response = clientException.response
        val data = gson.fromJson(response.content.readUTF8Line(), HttpResponse::class.java)
        throw Exception("错误码: ${data.code}, 错误信息: ${data.msg}")
    }
}