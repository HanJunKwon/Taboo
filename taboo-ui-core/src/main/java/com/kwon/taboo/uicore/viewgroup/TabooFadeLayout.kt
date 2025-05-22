package com.kwon.taboo.uicore.viewgroup

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.IntDef
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import com.kwon.taboo.uicore.R
import com.kwon.taboo.uicore.util.ResourceUtils

class TabooFadeLayout(context: Context, attrs: AttributeSet): ConstraintLayout (context, attrs) {
    @FadePosition
    private var fadePosition = FADE_POSITION_BOTTOM
    private var fadeHeight = 0f

    private var contentView = LinearLayout(context).apply {
        id = View.generateViewId()
        layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT
        )
    }
    private var topFadeView: View? = null
    private var bottomFadeView: View? = null

    init {
        initTabooFadeLayout()

        super.addView(contentView)

        val typed = context.obtainStyledAttributes(attrs, R.styleable.TabooFadeLayout)
        val fadePosition = typed.getInt(R.styleable.TabooFadeLayout_fadePosition,
            FADE_POSITION_BOTTOM
        )
        val fadeHeight = typed.getDimension(R.styleable.TabooFadeLayout_fadeHeight, 30f)
        typed.recycle()

        setFadePositionInternal(fadePosition)
        setFadeHeightInternal(fadeHeight)

        draw()
    }

    private fun initTabooFadeLayout() {
        id = generateViewId()
        isClickable = false
        isFocusable = false
        layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT
        )
    }

    override fun addView(child: View?) {
        if (child === topFadeView || child === bottomFadeView || child === contentView) {
            super.addView(child)
        } else {
            contentView.addView(child)
        }
    }

    override fun addView(child: View?, index: Int) {
        if (child === topFadeView || child === bottomFadeView || child === contentView) {
            super.addView(child, index)
        } else {
            contentView.addView(child, index)
        }
    }

    override fun addView(child: View?, params: ViewGroup.LayoutParams?) {
        if (child === topFadeView || child === bottomFadeView || child === contentView) {
            super.addView(child, params)
        } else {
            contentView.addView(child, params)
        }
    }

    override fun addView(child: View?, index: Int, params: ViewGroup.LayoutParams?) {
        if (child === topFadeView || child === bottomFadeView || child === contentView) {
            super.addView(child, index, params)
        } else {
            contentView.addView(child, index, params)
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
            removeView(it)
            topFadeView = null
        }

        bottomFadeView?.let {
            removeView(it)
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
            clone(this@TabooFadeLayout)
            connect(
                fadeView.id,
                constraintPosition,
                this@TabooFadeLayout.id,
                constraintPosition
            )
            applyTo(this@TabooFadeLayout)
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
         * Fade View 의 위치를 **상단**에 설정.
         */
        const val FADE_POSITION_TOP = 0

        /**
         * Fade View 의 위치를 **하단**에 설정.
         */
        const val FADE_POSITION_BOTTOM = 1

        /**
         * Fade View 의 위치를 **상단과 하단**에 설정.
         */
        const val FADE_POSITION_BOTH = 2
    }
}