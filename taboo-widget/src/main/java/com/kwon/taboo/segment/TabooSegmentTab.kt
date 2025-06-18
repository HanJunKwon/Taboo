package com.kwon.taboo.segment

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.LinearLayout
import android.widget.LinearLayout.HORIZONTAL
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isNotEmpty
import com.kwon.taboo.R
import com.kwon.taboo.uicore.util.ResourceUtils

class TabooSegmentTab @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): ConstraintLayout(context, attrs, defStyleAttr) {
    private var tabLayout = LinearLayout(context).apply {
        id = generateViewId()
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        orientation = HORIZONTAL
        background = ContextCompat.getDrawable(context, android.R.color.transparent)
    }

    private var selectorContainer = LinearLayout(context).apply {
        id = generateViewId()
        layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT)
        background = ContextCompat.getDrawable(context, R.drawable.shape_taboo_segment_selector_background)
    }
    private var isInitSElectedContainer = false

    private var items = mutableListOf<String>()

    private var selectedIndex = -1

    init {
        background = ContextCompat.getDrawable(context, R.drawable.shape_taboo_segment_tab_background)
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        val padding = ResourceUtils.dpToPx(context, 4f)
        setPadding(padding, padding, padding, padding)

        addView(selectorContainer)
        addView(tabLayout)

        viewTreeObserver.addOnGlobalLayoutListener {
            updateSelectedContainer()
        }
    }

    fun setItems(items: List<String>) {
        this.items.clear()
        this.items.addAll(items)
        tabLayout.removeAllViews()
        tabLayout.weightSum = items.size.toFloat()

        items.forEachIndexed { index, item ->
            val itemView = TextView(context).apply {
                text = item
                textAlignment = TEXT_ALIGNMENT_CENTER
                layoutParams = LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1f)
                val padding = ResourceUtils.dpToPx(context, 8f)
                setPadding(padding, padding, padding, padding)
                setTypeface(ResourcesCompat.getFont(context, com.kwon.taboo.uicore.R.font.font_pretendard_bold))
                setTextColor(ContextCompat.getColorStateList(context, R.color.selector_segment_tab_text_color))
                setOnClickListener {
                    this.isSelected = true

                    moveSelectorContainer(index)
                }
            }
            tabLayout.addView(itemView)
        }
    }

    private fun updateSelectedContainer() {
        if (tabLayout.isNotEmpty() && !isInitSElectedContainer) {
            val firstChild = tabLayout.getChildAt(0)
            selectorContainer.x = paddingStart.toFloat()
            selectorContainer.layoutParams.width = firstChild.width
            selectorContainer.layoutParams.height = firstChild.height
            selectorContainer.requestLayout()

            isInitSElectedContainer = true
        }
    }

    private fun moveSelectorContainer(index: Int) {
        if (index < 0 || index >= tabLayout.childCount) return

        selectedIndex = index

        var fromX = selectorContainer.x
        var toX = tabLayout.getChildAt(selectedIndex).x + paddingStart
        ValueAnimator
            .ofFloat(fromX, toX)
            .apply {
                duration = 200L
                addUpdateListener { animation ->
                    selectorContainer.x = animation.animatedValue as Float
                }
                start()
            }
    }
}