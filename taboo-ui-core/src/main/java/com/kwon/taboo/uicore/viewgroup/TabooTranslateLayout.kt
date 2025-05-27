package com.kwon.taboo.uicore.viewgroup

import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.core.animation.doOnStart
import androidx.core.content.withStyledAttributes
import com.kwon.taboo.uicore.R
import com.kwon.taboo.uicore.viewgroup.TabooTranslateLayout.TranslationDirection.Companion.BOTTOM_UP
import com.kwon.taboo.uicore.viewgroup.TabooTranslateLayout.TranslationDirection.Companion.LEFT_RIGHT
import com.kwon.taboo.uicore.viewgroup.TabooTranslateLayout.TranslationDirection.Companion.RIGHT_LEFT
import com.kwon.taboo.uicore.viewgroup.TabooTranslateLayout.TranslationDirection.Companion.TOP_DOWN

class TabooTranslateLayout(context: Context, attrs: AttributeSet): FrameLayout(context, attrs) {
    private var animationStarted = false

    private var translateAnimator: ObjectAnimator? = null

    private var translationDirection = BOTTOM_UP

    private var fromDelta = ANIMATION_FROM_DELTA

    private var toDelta = ANIMATION_TO_DELTA

    private var animDuration = ANIMATION_DURATION

    init {
        layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT
        )

        context.withStyledAttributes(attrs, R.styleable.TabooTranslateLayout) {
            val translationDirection = getInt(R.styleable.TabooTranslateLayout_translationDirection, BOTTOM_UP)
            val fromDelta = getFloat(R.styleable.TabooTranslateLayout_fromDelta, ANIMATION_FROM_DELTA)
            val toDelta = getFloat(R.styleable.TabooTranslateLayout_toDelta, ANIMATION_TO_DELTA)
            val animDuration = getInteger(R.styleable.TabooTranslateLayout_animationDuration, ANIMATION_DURATION)

            setTranslationDirection(translationDirection)
            setFromDelta(fromDelta)
            setToDelta(toDelta)
            setAnimDuration(animDuration)
        }
    }

    override fun addView(child: View?) {
        check(childCount < 1) { "TabooTranslateLayout can host only one direct child" }

        super.addView(child)
    }

    override fun addView(child: View, index: Int) {
        check(childCount < 1) { "TabooTranslateLayout can host only one direct child" }

        super.addView(child, index)
    }

    override fun addView(child: View?, params: ViewGroup.LayoutParams?) {
        check(childCount < 1) { "TabooTranslateLayout can host only one direct child" }

        super.addView(child, params)
    }

    override fun addView(child: View?, index: Int, params: ViewGroup.LayoutParams?) {
        check(childCount < 1) { "TabooTranslateLayout can host only one direct child" }

        super.addView(child, index, params)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        if (widthMode == MeasureSpec.EXACTLY || heightMode == MeasureSpec.EXACTLY) {
            val width = MeasureSpec.getSize(widthMeasureSpec)
            val height = MeasureSpec.getSize(heightMeasureSpec)

            val fromOffset = when (translationDirection) {
                BOTTOM_UP -> fromDelta * height
                TOP_DOWN -> -fromDelta * height
                LEFT_RIGHT -> -fromDelta * width
                RIGHT_LEFT -> fromDelta * width
                else -> 0f
            }

            val toOffset = when (translationDirection) {
                BOTTOM_UP -> toDelta * height
                TOP_DOWN -> -toDelta * height
                LEFT_RIGHT -> -toDelta * width
                RIGHT_LEFT -> toDelta * width
                else -> 0f
            }

            createAnimation(
                translationDirection,
                fromOffset,
                toOffset,
                animDuration
            )
        }
    }

    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        super.onWindowFocusChanged(hasWindowFocus)

        if (hasWindowFocus && !animationStarted) {
            translateAnimator?.let { animator ->
                animator.doOnStart {
                    animationStarted = true
                }
                animator.start()
            }
        }
    }

    fun setTranslationDirection(translationDirection: Int) {
        this.translationDirection = translationDirection
    }

    fun setFromDelta(fromDelta: Float) {
        this.fromDelta = fromDelta
    }

    fun setToDelta(toDelta: Float) {
        this.toDelta = toDelta
    }

    fun setAnimDuration(animDuration: Int) {
        this.animDuration = animDuration
    }

    private fun createAnimation(translationDirection: Int, fromOffset: Float, toOffset: Float, animDuration: Int) {
        val propertyName = when (translationDirection) {
            BOTTOM_UP,
            TOP_DOWN -> "translationY"
            else -> "translationX"
        }

        translateAnimator = ObjectAnimator
            .ofFloat(getChildAt(0), propertyName, fromOffset, toOffset)
            .apply {
                duration = animDuration.toLong()
                interpolator = DecelerateInterpolator()
            }
    }

    annotation class TranslationDirection {
        companion object {
            const val BOTTOM_UP = 0
            const val TOP_DOWN = 1
            const val LEFT_RIGHT = 2
            const val RIGHT_LEFT = 3
        }
    }

    companion object {
        const val ANIMATION_DURATION = 400 // Default animation duration in milliseconds

        const val ANIMATION_FROM_DELTA = 0.1f // Default from delta in percent
        const val ANIMATION_TO_DELTA = 0f // Default to delta in percent
    }
}