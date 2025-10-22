package com.kwon.taboo.uicore.toast.presenter

import android.content.Context
import android.graphics.PixelFormat
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import androidx.core.animation.doOnEnd
import com.kwon.taboo.uicore.toast.ToastSlideAnimator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

open class SlideToastPresenter(private val context: Context) {
    private var windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

    private var view: View? = null
    private var position = ToastPosition.TOP

    private var toastSlideAnimator = ToastSlideAnimator(context)

    fun show(view: View, @ToastPosition position: Int, @ToastDuration duration: Int) {
        this.view = view
        this.position = position

        windowManager.addView(this.view, createLayoutParams(position))
        toastSlideAnimator
            .createSlideInAnimation(view, position)
            .apply {
                doOnEnd {
                    CoroutineScope(Dispatchers.Main).launch {
                        when (duration) {
                            ToastDuration.SHORT -> delay(2_000L)
                            ToastDuration.LONG -> delay(3_500L)
                        }

                        hide()
                    }
                }
            }
            .start()
    }

    fun hide() {
        if (this.view != null) {
            toastSlideAnimator
                .createSlideOutAnimation(this.view!!, position)
                .apply {
                    doOnEnd {
                        windowManager.removeView(view)
                    }
                }
                .start()
        }
    }

    private fun createLayoutParams(@ToastPosition position: Int) : WindowManager.LayoutParams {
        return WindowManager.LayoutParams().apply {
            width = WindowManager.LayoutParams.WRAP_CONTENT
            height = WindowManager.LayoutParams.WRAP_CONTENT
            gravity = when (position) {
                ToastPosition.TOP -> Gravity.CENTER_HORIZONTAL or Gravity.TOP
                ToastPosition.BOTTOM -> Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM
                else -> Gravity.CENTER_HORIZONTAL or Gravity.TOP
            }
            flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
            format = PixelFormat.TRANSLUCENT
        }
    }

    annotation class ToastPosition {
        companion object {
            const val TOP = 0
            const val BOTTOM = 1
        }
    }

    annotation class ToastDuration {
        companion object {
            const val SHORT = 0
            const val LONG = 1
        }
    }
}