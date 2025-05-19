package com.kwon.taboo.uicore.viewgroup

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import com.kwon.taboo.uicore.R
import com.kwon.taboo.uicore.util.ResourceUtils

class TabooFadeScrollView(context: Context, attrs: AttributeSet): ScrollView(context, attrs) {
    private var fadeHeight = 0f
    private var wrapper = ConstraintLayout(context).apply {
        id = View.generateViewId()
        layoutParams = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.MATCH_PARENT
        )
    }

    private var contentContainer = LinearLayout(context).apply {
        id = View.generateViewId()
        layoutParams = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.MATCH_PARENT
        )
        orientation = LinearLayout.VERTICAL
    }

    private var fadeContainer: LinearLayout = LinearLayout(context).apply {
        id = View.generateViewId()
        layoutParams = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ResourceUtils.dpToPx(context, 25f)
        )
        background = ContextCompat.getDrawable(context, R.drawable.gradient_scroll_view_fade)
        orientation = LinearLayout.VERTICAL
    }

    init {
        setWillNotDraw(false)

        wrapper.addView(contentContainer)
        wrapper.addView(fadeContainer)

        ConstraintSet().apply {
            clone(wrapper)
            this.connect(
                fadeContainer.id,
                ConstraintSet.BOTTOM,
                wrapper.id,
                ConstraintSet.BOTTOM
            )
            applyTo(wrapper)
        }

        addView(wrapper)
    }

    override fun addView(child: View?) {
        if (child == wrapper) {
            super.addView(child)
        } else {
            contentContainer.addView(child)
        }
    }

    override fun addView(child: View?, index: Int) {
        if (child == wrapper) {
            if (childCount > 0) {
                return
            } else {
                super.addView(child, index)
            }
        } else {
            contentContainer.addView(child, index)
        }
    }

    override fun addView(child: View?, params: ViewGroup.LayoutParams?) {
        if (child == wrapper) {
            if (childCount > 0) {
                return
            } else {
                super.addView(child, params)
            }
        } else {
            contentContainer.addView(child, params)
        }
    }

    override fun addView(child: View?, index: Int, params: ViewGroup.LayoutParams?) {
        if (child == wrapper) {
            if (childCount > 0) {
                return
            } else {
                super.addView(child, index, params)
            }
        } else {
            contentContainer.addView(child, index, params)
        }
    }
}