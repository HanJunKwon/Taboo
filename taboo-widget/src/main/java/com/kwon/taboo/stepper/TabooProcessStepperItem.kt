package com.kwon.taboo.stepper

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import com.kwon.taboo.uicore.util.ResourceUtils

class TabooProcessStepperItem @JvmOverloads constructor(context: Context) : View(context) {
    private var isIndication = false

    @ColorInt
    private var trackColor = ContextCompat.getColor(context, com.kwon.taboo.uicore.R.color.taboo_black_800)

    @ColorInt
    private var indicatorColor = ContextCompat.getColor(context, com.kwon.taboo.uicore.R.color.taboo_blue_600)

    init {
        layoutParams = ViewGroup.MarginLayoutParams(
            ResourceUtils.dpToPx(context, 7f),
            ResourceUtils.dpToPx(context, 7f),
        )

        background = GradientDrawable().apply {
            setColor(trackColor)
            cornerRadius = 10f
        }
    }

    fun setIndicate(isIndication: Boolean) {
        if (this.isIndication == isIndication) return

        val backgroundColor = if (isIndication) indicatorColor else trackColor
        val targetWidth = if (isIndication) 50f else 7f

        background = (background as GradientDrawable).apply {
            setColor(backgroundColor)
        }

        ValueAnimator.ofInt(width, ResourceUtils.dpToPx(context, targetWidth)).apply {
            addUpdateListener {
                layoutParams = layoutParams.apply { width = it.animatedValue as Int }
            }
        }.start()

        this.isIndication = isIndication
    }

    fun setSpacing(spacing: Int) {
        layoutParams = (layoutParams as ViewGroup.MarginLayoutParams).apply {
            marginEnd = spacing
        }
    }

    fun setTrackColor(@ColorInt color: Int) {
        this.trackColor = color

        updateTrackColor()
    }

    private fun updateTrackColor() {
        if (!isIndication) {
            background = (background as GradientDrawable).apply {
                setColor(trackColor)
            }
        }
    }

    fun setIndicatorColor(@ColorInt color: Int) {
        this.indicatorColor = color

        updateIndicatorColor()
    }

    private fun updateIndicatorColor() {
        if (isIndication) {
            background = (background as GradientDrawable).apply {
                setColor(indicatorColor)
            }
        }
    }
}