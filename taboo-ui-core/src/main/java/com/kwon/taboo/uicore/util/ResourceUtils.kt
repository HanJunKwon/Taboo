package com.kwon.taboo.uicore.util

import android.content.Context
import android.util.TypedValue

object ResourceUtils {
    fun pxToDp(context: Context, px: Int): Float {
        val density = context.resources.displayMetrics.density
        return px / density
    }

    fun dpToPx(context: Context, dp: Float): Int {
        val density = context.resources.displayMetrics.density
        return (dp * density).toInt()
    }

    fun spToPx(context: Context, sp: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            sp,
            context.resources.displayMetrics
        )
    }
}