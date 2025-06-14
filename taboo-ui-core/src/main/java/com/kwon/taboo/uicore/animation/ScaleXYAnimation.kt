package com.kwon.taboo.uicore.animation

import android.R
import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.AnimationSet
import android.view.animation.Interpolator
import androidx.core.animation.addListener

class ScaleXYAnimation(private val view: View) {
    private val scaleValues = floatArrayOf(1f, 1f)

    private var duration = 100L

    private var interpolator: Interpolator = AccelerateInterpolator()

    private var scaleXAnimator: ObjectAnimator? = null
    private var scaleYAnimator: ObjectAnimator? = null

    private var listener: ScaleAnimatorListener? = null

    fun setScaleXY(startValue: Float, endValue: Float) : ScaleXYAnimation {
        check (startValue >= 0f && startValue <= 1f) {
            throw IllegalArgumentException("")
        }

        check (endValue >= 0f && endValue <= 1f) {
            throw IllegalArgumentException("")
        }

        scaleValues[0] = startValue
        scaleValues[1] = endValue

        updateScaleXYAnimation()

        return this
    }

    fun getScaleXY() = scaleValues

    private fun updateScaleXYAnimation() {
        scaleXAnimator = ObjectAnimator
            .ofFloat(view, SCALE_X, scaleValues[0], scaleValues[1])
            .apply {
                duration = this@ScaleXYAnimation.duration
                interpolator = this@ScaleXYAnimation.interpolator
            }

        scaleYAnimator = ObjectAnimator
            .ofFloat(view, SCALE_Y, scaleValues[0], scaleValues[1])
            .apply {
                duration = this@ScaleXYAnimation.duration
                interpolator = this@ScaleXYAnimation.interpolator
            }
    }

    fun setDuration(duration: Long) : ScaleXYAnimation {
        this.duration = duration

        return this
    }

    fun getDuration() = duration

    fun start() {
        AnimatorSet().apply {
            playTogether(scaleYAnimator, scaleXAnimator)

            addListener(onStart = { listener?.onScaleAnimationStart() })
            addListener(onEnd = { listener?.onScaleAnimationEnd() })
        }.start()
    }

    fun addListener(listener: ScaleAnimatorListener?) {
        this.listener = listener
    }

    interface ScaleAnimatorListener {
        fun onScaleAnimationStart()
        fun onScaleAnimationEnd()
    }

    companion object {
        const val SCALE_X = "scaleX"
        const val SCALE_Y = "scaleY"
    }
}