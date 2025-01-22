package com.kwon.taboo.calender

import com.kwon.utils.calendar.CalendarUtils

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
}