package com.kwon.taboo.calender

import com.kwon.utils.calendar.CalendarUtils
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

data class CalendarBlock(
    val timestamp: Long = 0,
    private val isToday: Boolean = false,
) {
    fun getDay(language: String): String {
        return CalendarUtils.getDay(timestamp, language)
    }

    fun getDate(): String {
        return CalendarUtils.getDate(timestamp)
    }

    /**
     * 연도, 월, 일을 가져온다.
     */
    fun getFullDate(): String {
        val cal: Calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        cal.timeInMillis = timestamp

        return dateFormat.format(cal.time)
    }
}