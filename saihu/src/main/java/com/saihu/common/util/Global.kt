package com.saihu.common.util

import android.content.Context
import com.saihu.common.data.model.Account

open class Global {
    private val TAG = "Global"
    open var user: Account? = null
        set(value) {
            field = value
            SpUtil.saveAccount(field)
        }
        get() = field ?: SpUtil.getAccount()

    open fun init(context: Context) {
        ToastUtil.init(context)
        SpUtil.init(context)
    }
}