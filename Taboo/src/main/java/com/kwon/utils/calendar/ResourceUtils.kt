package com.kwon.utils.calendar

import android.content.Context

object ResourceUtils {
    fun pxToDp(context: Context, px: Int): Float {
        val density = context.resources.displayMetrics.density
        return px / density
    }

    fun dpToPx(context: Context, dp: Float): Float {
        val density = context.resources.displayMetrics.density
        return dp * density
    }
}