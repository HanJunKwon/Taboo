package com.kwon.taboo.uicore.toast

import android.animation.ValueAnimator
import android.content.Context
import android.view.View
import com.kwon.taboo.uicore.toast.presenter.SlideToastPresenter
import com.kwon.taboo.uicore.util.ResourceUtils

private const val SLIDE_ANIMATION_DURATION = 300L
class ToastSlideAnimator(private val context: Context) {
    fun createSlideInAnimation(
        view: View,
        @SlideToastPresenter.ToastPosition position: Int,
    ) : ValueAnimator {
        val startYPosition = when (position) {
            SlideToastPresenter.ToastPosition.TOP -> ResourceUtils.dpToPx(context, -200f)
            else -> ResourceUtils.dpToPx(context, 200f)
        }

        val endYPosition = when (position) {
            SlideToastPresenter.ToastPosition.TOP -> ResourceUtils.dpToPx(context, 25f)
            else -> 0
        }

        return ValueAnimator.ofInt(startYPosition, endYPosition).apply {
            duration = SLIDE_ANIMATION_DURATION
            addUpdateListener {
                view.translationY = (it.animatedValue as Int).toFloat()
            }
        }
    }

    fun createSlideOutAnimation(
        view: View,
        @SlideToastPresenter.ToastPosition position: Int
    ) : ValueAnimator {
        val startYPosition = when (position) {
            SlideToastPresenter.ToastPosition.TOP -> ResourceUtils.dpToPx(context, 25f)
            else -> 0
        }

        val endYPosition = when (position) {
            SlideToastPresenter.ToastPosition.TOP -> ResourceUtils.dpToPx(context, -200f)
            else -> ResourceUtils.dpToPx(context, 200f)
        }

        return ValueAnimator.ofInt(startYPosition, endYPosition).apply {
            duration = SLIDE_ANIMATION_DURATION
            addUpdateListener {
                view.translationY = (it.animatedValue as Int).toFloat()
            }
        }
    }
}