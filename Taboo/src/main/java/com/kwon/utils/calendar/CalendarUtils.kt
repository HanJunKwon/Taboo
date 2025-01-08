package com.kwon.utils.calendar

import java.util.Calendar

object CalendarUtils {
    const val KOREAN = "ko"
    const val ENGLISH = "en"
    const val VIETNAMESE = "vn"

    private const val SUNDAY = 1
    private const val MONDAY = 2
    private const val TUESDAY = 3
    private const val WEDNESDAY = 4
    private const val THURSDAY = 5
    private const val FRIDAY = 6
    private const val SATURDAY = 7

    fun getDay(timestamp: Long) : Int {
        val cal: Calendar = Calendar.getInstance()
        cal.timeInMillis = timestamp
        val nWeek: Int = cal.get(Calendar.DAY_OF_WEEK)
        return nWeek
    }

    fun getDay(timestamp: Long, language: String) : String {
        val day = getDay(timestamp)
        return when (day) {
            SUNDAY -> getSundayLocale(language)
            MONDAY -> getMondayLocale(language)
            TUESDAY -> getTuesdayLocale(language)
            WEDNESDAY -> getWednesdayLocale(language)
            THURSDAY -> getThursdayLocale(language)
            FRIDAY -> getFridayLocale(language)
            SATURDAY -> getSaturdayLocale(language)
            else -> ""
        }
    }

    private fun getSundayLocale(language: String) : String {
        return when (language) {
            KOREAN -> "일"
            ENGLISH -> "S"
            VIETNAMESE -> "C"
            else -> ""
        }
    }

    private fun getMondayLocale(language: String) : String {
        return when (language) {
            KOREAN -> "월"
            ENGLISH -> "M"
            VIETNAMESE -> "H"
            else -> ""
        }
    }

    private fun getTuesdayLocale(language: String) : String {
        return when (language) {
            KOREAN -> "화"
            ENGLISH -> "T"
            VIETNAMESE -> "B"
            else -> ""
        }
    }

    private fun getWednesdayLocale(language: String) : String {
        return when (language) {
            KOREAN -> "수"
            ENGLISH -> "W"
            VIETNAMESE -> "T"
            else -> ""
        }
    }

    private fun getThursdayLocale(language: String) : String {
        return when (language) {
            KOREAN -> "목"
            ENGLISH -> "T"
            VIETNAMESE -> "N"
            else -> ""
        }
    }

    private fun getFridayLocale(language: String) : String {
        return when (language) {
            KOREAN -> "금"
            ENGLISH -> "F"
            VIETNAMESE -> "S"
            else -> ""
        }
    }

    private fun getSaturdayLocale(language: String) : String {
        return when (language) {
            KOREAN -> "토"
            ENGLISH -> "S"
            VIETNAMESE -> "B"
            else -> ""
        }
    }

    fun getDate(timestamp: Long) : String {
        val cal: Calendar = Calendar.getInstance()
        cal.timeInMillis = timestamp
        return cal.get(Calendar.DATE).toString()
    }

}