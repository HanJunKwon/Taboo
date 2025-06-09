package com.kwon.taboo.uicore.attribute

import android.graphics.drawable.GradientDrawable

class FadeAppearance {
    private var gradientColors = intArrayOf()

    @FadeOrientation
    private var fadeOrientation = FadeOrientation.TOP_BOTTOM

    fun setGradientColors(startColor: Int, endColor: Int) {
        gradientColors = intArrayOf(startColor, endColor)
    }

    fun setGradientColors(colors: IntArray) {
        gradientColors = colors
    }

    fun setGradientOrientation(@FadeOrientation orientation: Int) {
        fadeOrientation = orientation
    }

    fun getGradientDrawable(): GradientDrawable {
        val orientation = when (fadeOrientation) {
            FadeOrientation.TOP_BOTTOM -> GradientDrawable.Orientation.TOP_BOTTOM
            FadeOrientation.BOTTOM_TOP -> GradientDrawable.Orientation.BOTTOM_TOP
            FadeOrientation.LEFT_RIGHT -> GradientDrawable.Orientation.LEFT_RIGHT
            FadeOrientation.RIGHT_LEFT -> GradientDrawable.Orientation.RIGHT_LEFT
            else -> GradientDrawable.Orientation.TOP_BOTTOM // 기본값
        }

        return GradientDrawable(
            orientation,
            gradientColors
        )
    }

    @Retention
    annotation class FadeOrientation {
        companion object {
            const val TOP_BOTTOM = 0
            const val BOTTOM_TOP = 1
            const val LEFT_RIGHT = 2
            const val RIGHT_LEFT = 3
        }
    }
}