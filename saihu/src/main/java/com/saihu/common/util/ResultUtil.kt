package com.saihu.common.util

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentActivity
import com.tbruyelle.rxpermissions3.Permission
import com.tbruyelle.rxpermissions3.RxPermissions
import io.reactivex.rxjava3.functions.Consumer
import kotlin.let


object ResultUtil {
    private val TAG = "ResultUtil"
    private lateinit var rxPermissions: RxPermissions

    fun init(activity: FragmentActivity) {
        rxPermissions = RxPermissions(activity)
        activityForResultLauncher =
            activity.registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) {
                if (it.resultCode == Activity.RESULT_OK) {
                    it.data?.let { intent ->
                        try {
                            this.onResult.accept(intent)
                        } catch (_: Throwable) {
                        }
                    }
                }
            }
    }

    fun requestPermissions(onResult: Consumer<Permission>, vararg permissions: String) {
        val disposable =
            rxPermissions.requestEachCombined(*permissions)
                .subscribe(onResult,
                    { t -> Log.e(TAG, "授权报错", t) },
                    { Log.i(TAG, "授权完成") }
                )
    }

    private lateinit var activityForResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var onResult: Consumer<Intent>
    fun openForResult(intent: Intent, onResult: Consumer<Intent>) {
        this.onResult = onResult
        activityForResultLauncher.launch(intent)
    }
}