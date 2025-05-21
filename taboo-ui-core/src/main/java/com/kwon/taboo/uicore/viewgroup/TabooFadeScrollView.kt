package com.kwon.taboo.uicore.viewgroup

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.IntDef
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import com.kwon.taboo.uicore.R
import com.kwon.taboo.uicore.util.ResourceUtils

class TabooFadeScrollView(context: Context, attrs: AttributeSet): TabooScrollViewCore(context, attrs) {
    @FadePosition
    private var fadePosition = FADE_POSITION_BOTTOM
    private var fadeHeight = 0f

    private var fadeContainer: LinearLayout = LinearLayout(context).apply {
        id = View.generateViewId()
        layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT
        )
        background = ContextCompat.getDrawable(context, R.drawable.gradient_scroll_view_fade)
        orientation = LinearLayout.VERTICAL
    }

    init {
        super.addView(fadeContainer)

        val typed = context.obtainStyledAttributes(attrs, R.styleable.TabooFadeScrollView)
        val fadePosition = typed.getInt(R.styleable.TabooFadeScrollView_fadePosition, FADE_POSITION_BOTTOM)
        val fadeHeight = typed.getDimension(R.styleable.TabooFadeScrollView_fadeHeight, 30f)
        typed.recycle()

        setFadePositionInternal(fadePosition)
        setFadeHeightInternal(fadeHeight)

        draw()
    }

    override fun addView(child: View?) {
        if (child === fadeContainer || child === scrollView) {
            super.addView(child)
        } else {
            scrollView.addView(child)
        }
    }

    override fun addView(child: View?, index: Int) {
        if (child === fadeContainer || child === scrollView) {
            super.addView(child, index)
        } else {
            scrollView.addView(child, index)
        }
    }

    override fun addView(child: View?, params: ViewGroup.LayoutParams?) {
        if (child === fadeContainer || child === scrollView) {
            super.addView(child, params)
        } else {
            scrollView.addView(child, params)
        }
    }

    override fun addView(child: View?, index: Int, params: ViewGroup.LayoutParams?) {
        if (child === fadeContainer || child === scrollView) {
            super.addView(child, index, params)
        } else {
            scrollView.addView(child, index, params)
        }
    }

    private fun draw() {
        updateFadePosition()
        updateFadeHeight()
    }

    /**
     * Todo 추후에 현재 스크롤뷰의 Orientation에 따라 [fadePosition] 설정의 예외처리를 해줘야함.
     */
    private fun setFadePositionInternal(@FadePosition fadePosition: Int) {
        this.fadePosition = fadePosition
    }

    fun setFadePosition(@FadePosition fadePosition: Int) {
        setFadePositionInternal(fadePosition)

        updateFadePosition()
    }

    private fun updateFadePosition() {
        ConstraintSet().apply {
            clone(this@TabooFadeScrollView)
            connect(
                fadeContainer.id,
                ConstraintSet.BOTTOM,
                this@TabooFadeScrollView.id,
                ConstraintSet.BOTTOM
            )
            applyTo(this@TabooFadeScrollView)
        }
    }

    private fun setFadeHeightInternal(height: Float) {
        fadeHeight = height
    }

    fun setFadeHeight(height: Float) {
        setFadeHeightInternal(height)

        updateFadeHeight()
    }

    private fun updateFadeHeight() {
        fadeContainer.layoutParams.height = ResourceUtils.dpToPx(context, fadeHeight)
        fadeContainer.requestLayout()
    }

    @IntDef(
        FADE_POSITION_TOP,
        FADE_POSITION_BOTTOM,
        FADE_POSITION_BOTH
    )
    @Retention(AnnotationRetention.SOURCE)
    annotation class FadePosition

    companion object {
        /**
         * `orientation`이 `horizontal`일 때, **fade**의 위치를 **상단**에 설정.
         */
        const val FADE_POSITION_TOP = 0

        /**
         * `orientation`이 `horizontal`일 때, **fade**의 위치를 **하단**에 설정.
         */
        const val FADE_POSITION_BOTTOM = 1

        /**
         * `orientation`이 `horizontal`일 때, **fade**의 위치를 **시작**에 설정.
         */
        const val FADE_POSITION_BOTH = 2

        const val TOP_FADE_VIEW_INDEX = 0
        const val BOTTOM_FADE_VIEW_INDEX = 1
    }
}