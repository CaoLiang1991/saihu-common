package com.saihu.common.util

import android.content.Context
import android.widget.Toast
import java.lang.ref.WeakReference

object ToastUtil {
    private var toast: Toast? = null
    private lateinit var contextRef: WeakReference<Context>

    fun init(context: Context) {
        contextRef = WeakReference(context)
    }

    fun showToast(message: String) {
        val context = contextRef.get()
        toast?.cancel()
        toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast?.show()
    }
}