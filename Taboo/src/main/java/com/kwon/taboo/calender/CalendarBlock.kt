package com.kwon.taboo.calender

import com.kwon.utils.calendar.CalendarUtils
import java.util.Calendar

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
        cal.timeInMillis = timestamp
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH) + 1
        val day = cal.get(Calendar.DAY_OF_MONTH)

        return "$year$month$day"
    }
}