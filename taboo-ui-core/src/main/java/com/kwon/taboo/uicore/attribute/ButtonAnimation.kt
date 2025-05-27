package com.kwon.taboo.uicore.attribute

import android.view.animation.AccelerateInterpolator
import android.view.animation.Interpolator
import androidx.annotation.IntDef

class ButtonAnimation {
    @AnimationType
    private var animationType: Int = SCALE

    private var propertyNames: List<String> = listOf("scaleX, scaleY")

    private var duration = 100L

    private var startValue = 1f

    private var endValue = 0.95f

    private var interpolator: Interpolator = AccelerateInterpolator()

    fun setAnimationType(@AnimationType type: Int) {
        animationType = type
    }

    fun getAnimationType() = animationType

    private fun setPropertyNamesOfType() {
        propertyNames = when (animationType) {
            SCALE -> {
                listOf("scaleX", "scaleY")
            }
            else -> throw Exception("")
        }
    }

    fun getPropertyNames() = propertyNames

    fun setDuration(duration: Long) {
        this.duration = duration
    }

    fun getDuration() = duration

    fun setStartValue(value: Float) {
        this.startValue = value
    }

    fun getStartValue() = startValue

    fun setEndValue(value: Float) {
        this.endValue = value
    }

    fun getEndValue() = endValue

    fun setInterpolator(interpolator: Interpolator) {
        this.interpolator = interpolator
    }

    fun getInterpolator() = interpolator

    @IntDef(SCALE)
    annotation class AnimationType

    companion object {
        const val SCALE = 0
    }
}