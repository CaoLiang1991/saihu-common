package com.saihu.common.data.net

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.ApolloRequest
import com.apollographql.apollo.api.ApolloResponse
import com.apollographql.apollo.api.Mutation
import com.apollographql.apollo.api.Operation
import com.apollographql.apollo.api.Query
import com.apollographql.apollo.api.Subscription
import com.apollographql.apollo.exception.ApolloGraphQLException
import com.apollographql.apollo.exception.ApolloHttpException
import com.apollographql.apollo.exception.ApolloNetworkException
import com.apollographql.apollo.interceptor.ApolloInterceptor
import com.apollographql.apollo.interceptor.ApolloInterceptorChain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object ApolloUtil {
    private lateinit var apolloClient: ApolloClient
    
    fun init(hostUrl: String) {
        apolloClient = ApolloClient.Builder()
            .serverUrl(hostUrl)
            .addInterceptor(AuthorizationInterceptor())
            .build()
    }

    suspend fun <T : Query.Data> query(query: Query<T>): T {
        val response = apolloClient.query(query).execute()
        return handleResponse(response)
    }

    suspend fun <T : Mutation.Data> mutation(mutation: Mutation<T>): T {
        val response = apolloClient.mutation(mutation).execute()
        return handleResponse(response)
    }

    fun <T : Subscription.Data> subscription(subscription: Subscription<T>): Flow<T> {
        val flow = apolloClient.subscription(subscription).toFlow()
        return flow.map { response ->
            return@map handleResponse(response)
        }
    }
}

private fun <T : Operation.Data> handleResponse(response: ApolloResponse<T>): T {
    val data = response.data
    if (data != null) {
        return data
    } else {
        if (response.exception != null) {
            when (response.exception) {
                is ApolloGraphQLException -> throw Exception((response.exception as ApolloGraphQLException).error.message)
                is ApolloHttpException -> {
                    val e = response.exception as ApolloHttpException
                    throw Exception("错误码: " + e.statusCode + ", 错误信息: " + e.message)
                }

                is ApolloNetworkException -> throw Exception((response.exception as ApolloNetworkException).message)
                else -> throw Exception(response.exception!!.message)
            }
        } else if (response.hasErrors()) {
            throw Exception(response.errors!![0].message)
        }
        throw Exception("未知错误")
    }
}


private class AuthorizationInterceptor : ApolloInterceptor {
    override fun <T : Operation.Data> intercept(
        request: ApolloRequest<T>,
        chain: ApolloInterceptorChain
    ): Flow<ApolloResponse<T>> {
        return chain.proceed(
            request.newBuilder()
                .apply {
                    this.addHttpHeader("x-hasura-admin-secret", "Jones88888!")
//                Global.account?.let { account ->
//                    this.addHttpHeader("Authorization", "Bearer ${account.token.token}")
//                }
                }
                .build())
    }
}