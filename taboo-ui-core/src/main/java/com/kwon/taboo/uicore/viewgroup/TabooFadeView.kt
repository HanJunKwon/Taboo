package com.kwon.taboo.uicore.viewgroup

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes
import com.kwon.taboo.uicore.R
import com.kwon.taboo.uicore.attribute.FadeAppearance

class TabooFadeView(context: Context, attrs: AttributeSet): View(context, attrs) {
    private var fadeAppearance = FadeAppearance()

    init {
        context.withStyledAttributes(attrs, R.styleable.TabooFadeView) {
            setGradientColorsInternal(
                startColor = getColor(R.styleable.TabooFadeView_startColor, ContextCompat.getColor(context, R.color.taboo_fade_view_default_start_color)),
                endColor = getColor(R.styleable.TabooFadeView_endColor, ContextCompat.getColor(context, R.color.taboo_fade_view_default_end_color))
            )

            setFadeOrientationInternal(getInt(R.styleable.TabooFadeView_fadeOrientation, FadeAppearance.FadeOrientation.TOP_BOTTOM))
        }
    }

    private fun setGradientColorsInternal(startColor: Int, endColor: Int) {
        fadeAppearance.setGradientColors(intArrayOf(startColor, endColor))
    }

    fun setGradientColors(startColor: Int, endColor: Int) {
        setGradientColorsInternal(startColor, endColor)

        invalidate()
    }

    private fun setFadeOrientationInternal(@FadeAppearance.FadeOrientation fadeOrientation: Int) {
        fadeAppearance.setGradientOrientation(fadeOrientation)
    }

    fun setFadeOrientation(@FadeAppearance.FadeOrientation fadeOrientation: Int) {
        setFadeOrientationInternal(fadeOrientation)

        // 새로 설정된 Orientation에 따라 뷰를 다시 그림
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Draw the gradient background
        background = fadeAppearance.getGradientDrawable()
    }
}