package com.saihu.common.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

open class DateUtil {

    open fun currentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
        return dateFormat.format(Date())
    }

    open fun currentDateTime(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
        return dateFormat.format(Date())
    }

    open fun format(dateMillis: Long): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
        return sdf.format(Date(dateMillis))
    }

    open fun format(timestampStr: String?): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.CHINA)
        val outputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA)
        return try {
            val date = timestampStr?.let { inputFormat.parse(it) }
            date?.let { outputFormat.format(it) } ?: ""
        } catch (e: Exception) {
            ""
        }
    }
}