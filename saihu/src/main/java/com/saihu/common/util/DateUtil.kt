package com.saihu.common.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateUtil {
    fun currentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
        return dateFormat.format(Date())
    }

    fun tomorrow(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        return dateFormat.format(calendar.time)
    }

    fun currentDateTime(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
        return dateFormat.format(Date())
    }

    fun format(dateMillis: Long): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
        return sdf.format(Date(dateMillis))
    }

    fun format(timestampStr: String?): String {
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