package com.saihu.common.util

import android.content.Context
import com.google.gson.Gson
import com.saihu.common.data.model.Account
import java.lang.ref.WeakReference
import androidx.core.content.edit

object SpUtil {
    private lateinit var contextRef: WeakReference<Context>
    private lateinit var gson: Gson

    fun init(context: Context) {
        contextRef = WeakReference(context)
        gson = Gson()
    }

    fun saveAccount(account: Account?) {
        var json = ""
        if (account != null) {
            json = gson.toJson(account);
        }
        saveString("account", json);
    }

    fun getAccount(): Account? {
        val json = getString("account");
        if (json.isEmpty()) return null
        return gson.fromJson(json, Account::class.java)
    }

    private fun saveString(key: String, value: String) {
        val context = contextRef.get()
        if (context != null) {
            val sharedPref = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
            sharedPref.edit {
                putString(key, value)
            }
        }
    }

    private fun getString(key: String): String {
        val context = contextRef.get()
        if (context != null) {
            val sharedPref = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
            return sharedPref.getString(key, "") ?: ""
        }
        return ""
    }
}