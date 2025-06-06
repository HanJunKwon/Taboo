package com.kwon.taboo.uicore.attribute

import android.content.res.ColorStateList
import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import kotlin.math.roundToInt

data class ColorContainer(
    @ColorInt var primaryColor: Int,
    @ColorInt var secondaryColor: Int
) {
    fun getPrimaryColorStateList(): ColorStateList {
        return ColorStateList(
            arrayOf(
                intArrayOf(android.R.attr.state_enabled),
                intArrayOf(-android.R.attr.state_enabled)
            ),
            intArrayOf(
                primaryColor,
                adjustAlpha(primaryColor, 0.4f)
            )
        )
    }

    fun getSecondaryColorStateList(): ColorStateList {
        return ColorStateList(
            arrayOf(
                intArrayOf(android.R.attr.state_enabled),
                intArrayOf(-android.R.attr.state_enabled)
            ),
            intArrayOf(
                secondaryColor,
                adjustAlpha(secondaryColor, 0.4f)
            )
        )
    }

    private fun adjustAlpha(@ColorInt color: Int, factor: Float): Int {
        val alpha = (Color.alpha(color) * factor).roundToInt()
        val red = Color.red(color)
        val green = Color.green(color)
        val blue = Color.blue(color)
        return Color.argb(alpha, red, green, blue)
    }
}