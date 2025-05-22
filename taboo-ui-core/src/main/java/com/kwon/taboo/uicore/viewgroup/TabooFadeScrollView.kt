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

    private var topFadeView: View? = null
    private var bottomFadeView: View? = null

    init {
        val typed = context.obtainStyledAttributes(attrs, R.styleable.TabooFadeScrollView)
        val fadePosition = typed.getInt(R.styleable.TabooFadeScrollView_fadePosition, FADE_POSITION_BOTTOM)
        val fadeHeight = typed.getDimension(R.styleable.TabooFadeScrollView_fadeHeight, 30f)
        typed.recycle()

        setFadePositionInternal(fadePosition)
        setFadeHeightInternal(fadeHeight)

        draw()
    }

    override fun addView(child: View?) {
        if (child === topFadeView || child === bottomFadeView || child === scrollView) {
            super.addView(child)
        } else {
            scrollView.addView(child)
        }
    }

    override fun addView(child: View?, index: Int) {
        if (child === topFadeView || child === bottomFadeView || child === scrollView) {
            super.addView(child, index)
        } else {
            scrollView.addView(child, index)
        }
    }

    override fun addView(child: View?, params: ViewGroup.LayoutParams?) {
        if (child === topFadeView || child === bottomFadeView || child === scrollView) {
            super.addView(child, params)
        } else {
            scrollView.addView(child, params)
        }
    }

    override fun addView(child: View?, index: Int, params: ViewGroup.LayoutParams?) {
        if (child === topFadeView || child === bottomFadeView || child === scrollView) {
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
        removeFadeViews()

        when (fadePosition) {
            FADE_POSITION_TOP -> {
                addTopFadeView()
            }
            FADE_POSITION_BOTTOM -> {
                addBottomFadeView()
            }
            FADE_POSITION_BOTH -> {
                addTopFadeView()
                addBottomFadeView()
            }
        }
    }

    private fun removeFadeViews() {
        topFadeView?.let {
            scrollView.removeView(it)
            topFadeView = null
        }

        bottomFadeView?.let {
            scrollView.removeView(it)
            bottomFadeView = null
        }
    }

    private fun createFadeView(isBottom: Boolean): View {
        return View(context).apply {
            id = View.generateViewId()
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                ResourceUtils.dpToPx(context, fadeHeight)
            )

            if (isBottom) background = ContextCompat.getDrawable(context, R.drawable.gradient_scroll_view_fade_top_to_bottom)
            else background = ContextCompat.getDrawable(context, R.drawable.gradient_scroll_view_fade_bottom_to_top)
        }
    }

    private fun addTopFadeView() {
        if (topFadeView == null) {
            topFadeView = createFadeView(false)
            super.addView(topFadeView)
        }

        constraintsFadeView(topFadeView!!, false)
    }

    private fun addBottomFadeView() {
        if (bottomFadeView == null) {
            bottomFadeView = createFadeView(true)
            super.addView(bottomFadeView)
        }

        constraintsFadeView(bottomFadeView!!, true)
    }

    private fun constraintsFadeView(fadeView: View, isBottom: Boolean) {
        val constraintPosition = if (isBottom) {
            ConstraintSet.BOTTOM
        } else {
            ConstraintSet.TOP
        }

        ConstraintSet().apply {
            clone(this@TabooFadeScrollView)
            connect(
                fadeView.id,
                constraintPosition,
                this@TabooFadeScrollView.id,
                constraintPosition
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
        topFadeView?.let {
            it.layoutParams.height = ResourceUtils.dpToPx(context, fadeHeight)
            it.requestLayout()
        }

        bottomFadeView?.let {
            it.layoutParams.height = ResourceUtils.dpToPx(context, fadeHeight)
            it.requestLayout()
        }
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
    }
}