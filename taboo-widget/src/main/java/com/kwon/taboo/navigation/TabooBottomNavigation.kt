package com.kwon.taboo.navigation

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.hansae.taboo.core.util.ResourceUtils
import com.kwon.taboo.R
import com.kwon.taboo.uicore.navigation.TabooNavigationCore

class TabooBottomNavigation(context: Context, attrs: AttributeSet): TabooNavigationCore(context, attrs) {
    init {
        createTopStrokeBackground()
        setPadding(0, ResourceUtils.dpToPx(context, 3f), 0, ResourceUtils.dpToPx(context, 3f))
    }

    private fun createTopStrokeBackground() {
        val gradient = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadii = floatArrayOf(
                30f, 30f, // top left
                30f, 30f, // top right
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
                    ContextCompat.getColor(context, R.color.taboo_bottom_navigation_background)
                )
            )
            setStroke(
                ResourceUtils.dpToPx(context, 1f),
                ContextCompat.getColor(context, R.color.taboo_bottom_navigation_stroke)
            )
        }

        // 3. LayerDrawable로 합치기
        val layerDrawable = LayerDrawable(arrayOf(gradient))

        // 3. Inset 설정 (단위: px)
        val bottomInset = (1 * context.resources.displayMetrics.density).toInt()
        val sideInset = (-5 * context.resources.displayMetrics.density).toInt()

        layerDrawable.setLayerInset(
            0, // index
            sideInset, // left
            bottomInset, // top
            sideInset, // right
            sideInset // bottom
        )

        background = layerDrawable
    }
}