package com.kwon.taboo.stepper

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.kwon.taboo.uicore.util.ResourceUtils

class TabooProcessStepperItem @JvmOverloads constructor(
    context: Context,
    spacing: Int = 0
) : View(context) {
    private var isIndication = false

    init {
        layoutParams = ViewGroup.MarginLayoutParams(
            ResourceUtils.dpToPx(context, 7f),
            ResourceUtils.dpToPx(context, 7f),
        ).apply {
            marginEnd = spacing
        }

        background = GradientDrawable().apply {
            setColor(ContextCompat.getColor(context, com.kwon.taboo.uicore.R.color.taboo_black_800))
            cornerRadius = 10f
        }
    }

    fun setIndicate(isIndication: Boolean) {
        if (this.isIndication == isIndication) return

        val backgroundColorResId = if (isIndication) com.kwon.taboo.uicore.R.color.taboo_blue_600 else com.kwon.taboo.uicore.R.color.taboo_black_800
        val targetWidth = if (isIndication) 50f else 7f

        background = (background as GradientDrawable).apply {
            setColor(ContextCompat.getColor(context, backgroundColorResId))
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
}