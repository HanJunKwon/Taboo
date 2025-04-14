package com.kwon.taboo.navigation

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.kwon.taboo.R
import com.kwon.taboo.uicore.navigation.TabooNavigationCore

class TabooBottomNavigation(context: Context, attrs: AttributeSet): TabooNavigationCore(context, attrs) {
    init {
        createBackground()
    }

    private fun createBackground() {
        val gradient = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadii = floatArrayOf(
                10f, 10f, // top left
                0f, 0f, // top right
                0f, 0f, // bottom right
                0f, 0f, // bottom left
                0f, 0f, // top left
                0f, 0f, // top right
                0f, 0f, // bottom right
                0f, 0f // bottom left
            )
            color = ColorStateList(
                arrayOf(intArrayOf()),
                intArrayOf(
                    ContextCompat.getColor(context, com.kwon.taboo.uicore.R.color.white)
                )
            )
            setStroke(
                1,
                ContextCompat.getColor(context, com.kwon.taboo.uicore.R.color.taboo_black_50)
            )
//            setPadding(-5, 0, -5, -5)
        }

        background = gradient
    }
}