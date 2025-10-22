package com.kwon.taboo.uicore.toast

import android.animation.ValueAnimator
import android.content.Context
import android.view.View
import com.kwon.taboo.uicore.toast.presenter.ToastPresenterCore
import com.kwon.taboo.uicore.util.ResourceUtils

private const val SLIDE_ANIMATION_DURATION = 300L
class ToastSlideAnimator(private val context: Context) {
    fun createSlideInAnimation(
        view: View,
        @ToastPresenterCore.ToastPosition position: Int,
    ) : ValueAnimator {
        val startYPosition = when (position) {
            ToastPresenterCore.ToastPosition.TOP -> -200
            else -> 200
        }

        val endYPosition = when (position) {
            ToastPresenterCore.ToastPosition.TOP -> ResourceUtils.dpToPx(context, 25f)
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
        @ToastPresenterCore.ToastPosition position: Int
    ) : ValueAnimator {
        val startYPosition = when (position) {
            ToastPresenterCore.ToastPosition.TOP -> ResourceUtils.dpToPx(context, 25f)
            else -> 0
        }

        val endYPosition = when (position) {
            ToastPresenterCore.ToastPosition.TOP -> -200
            else -> 200
        }

        return ValueAnimator.ofInt(startYPosition, endYPosition).apply {
            duration = SLIDE_ANIMATION_DURATION
            addUpdateListener {
                view.translationY = (it.animatedValue as Int).toFloat()
            }
        }
    }
}