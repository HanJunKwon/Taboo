package com.kwon.taboo.uicore.viewgroup

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import androidx.constraintlayout.widget.ConstraintLayout

open class TabooScrollViewCore(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {
    protected val scrollView = ScrollView(context)

    init {
        initTabooScrollViewCore()
        initScrollView()
    }

    private fun initTabooScrollViewCore() {
        id = View.generateViewId()
        isClickable = false
        isFocusable = false
        layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT
        )
    }

    private fun initScrollView() {
        scrollView.setWillNotDraw(false)
        scrollView.id = View.generateViewId()
        scrollView.isHorizontalScrollBarEnabled = false
        scrollView.isVerticalScrollBarEnabled = false
        scrollView.overScrollMode = OVER_SCROLL_NEVER
        scrollView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        scrollView.isFocusableInTouchMode = true
        scrollView.requestFocus()

        addView(scrollView)
    }
}