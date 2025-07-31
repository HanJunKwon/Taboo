package com.kwon.taboo.segment

import android.animation.ValueAnimator
import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.LinearLayout
import android.widget.LinearLayout.HORIZONTAL
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.content.withStyledAttributes
import androidx.core.util.TypedValueCompat.ComplexDimensionUnit
import androidx.core.view.children
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

    private var tabFontFamily = com.kwon.taboo.uicore.R.font.font_pretendard_regular
    private var tabTextColor = ContextCompat.getColorStateList(context, R.color.taboo_segment_tab_text_color)
    private var tabTextSizePixel = ResourceUtils.spToPx(context, 14f)
    private var tabPadding: Float = ResourceUtils.dpToPx(context, 8f).toFloat()

    private var isInitSElectedContainer = false

    private var items = mutableListOf<String>()

    private var selectedIndex = -1

    private var onTabSelectedListener: ((Int) -> Unit)? = null

    init {
        context.withStyledAttributes(attrs, R.styleable.TabooSegmentTab) {
            // Tab 텍스트 색상
            val tabTextColor = getColorStateList(R.styleable.TabooSegmentTab_tabTextColor)
                ?: ColorStateList.valueOf(ContextCompat.getColor(context, R.color.selector_taboo_segment_tab_text_color))
            setTabTextColor(tabTextColor)

            // Tab 텍스트 크기
            setTabTextSize(
                unit = TypedValue.COMPLEX_UNIT_PX,
                textSize = getDimensionPixelSize(R.styleable.TabooSegmentTab_tabTextSize,
                    ResourceUtils.spToPx(context, 14f).toInt()
                ).toFloat()
            )

            // Tab 폰트
            val tabFontFamily = getResourceId(
                R.styleable.TabooSegmentTab_tabFontFamily,
                com.kwon.taboo.uicore.R.font.font_pretendard_medium
            )
            setTabFont(tabFontFamily)

            // Tab의 패딩
            setTabPadding(getDimension(R.styleable.TabooSegmentTab_tabPadding, ResourceUtils.dpToPx(context, 8f).toFloat()))

            // Selector 색상
            setSelectorColor(getColor(
                R.styleable.TabooSegmentTab_selectorColor,
                ContextCompat.getColor(context, R.color.taboo_segment_tab_selector_background)
            ))
        }

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

    fun setTabTextColor(tabColorStateList: ColorStateList) {
        tabTextColor = tabColorStateList

        updateTabTextColor()
    }

    fun setTabTextColor(@ColorInt tabTextColorInt: Int) {
        this@TabooSegmentTab.tabTextColor = ColorStateList.valueOf(tabTextColorInt)

        updateTabTextColor()
    }

    private fun updateTabTextColor() {
        tabLayout.children.forEach {
            (it as TextView).setTextColor(tabTextColor)
        }
    }

    fun setTabTextSize(@ComplexDimensionUnit unit: Int = TypedValue.COMPLEX_UNIT_SP, textSize: Float) {
        tabTextSizePixel = if (unit == TypedValue.COMPLEX_UNIT_SP) ResourceUtils.spToPx(context, textSize) else textSize

        updateTabTextSize()
    }

    private fun updateTabTextSize() {
        tabLayout.children.forEach {
            (it as TextView).setTextSize(TypedValue.COMPLEX_UNIT_PX, tabTextSizePixel)
        }
    }

    fun setTabFont(fontFamily: Int) {
        tabFontFamily = fontFamily

        updateTabFont()
    }

    private fun updateTabFont() {
        tabLayout.children.forEach {
            (it as TextView).typeface = ResourcesCompat.getFont(context, tabFontFamily)
        }
    }

    fun setTabPadding(padding: Float) {
        tabPadding = padding

        tabLayout.children.forEach {
            it.setPadding(padding.toInt(), padding.toInt(), padding.toInt(), padding.toInt())
        }
    }

    fun setSelectorColor(selectorColor: Int) {
        selectorContainer.backgroundTintList = ColorStateList.valueOf(selectorColor)
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
                setTypeface(ResourcesCompat.getFont(context, tabFontFamily))
                setTextColor(tabTextColor)
                setTextSize(TypedValue.COMPLEX_UNIT_PX, tabTextSizePixel)
                setOnClickListener {
                    onTabSelectedListener?.invoke(index)

                    tabTextSelected(index)
                    moveSelectorContainer(index)
                }
            }
            tabLayout.addView(itemView)
        }
    }

    fun setSelectedIndex(index: Int) {
        onTabSelectedListener?.invoke(index)

        tabTextSelected(index)
        moveSelectorContainer(index)
    }

    fun setOnTabSelectedListener(listener: (Int) -> Unit) {
        onTabSelectedListener = listener
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

    private fun tabTextSelected(index: Int) {
        tabLayout.children.forEachIndexed { i, view ->
            (view as TextView).isSelected = (i == index)
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

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY) {
            setSelectedIndex(selectedIndex)
        }
    }
}