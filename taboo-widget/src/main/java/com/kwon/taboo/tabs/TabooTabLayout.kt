package com.kwon.taboo.tabs

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kwon.taboo.R
import com.kwon.taboo.adapter.TabooTabAdapter

class TabooTabLayout(
    context: Context,
    attrs: AttributeSet
) : RecyclerView(context, attrs) {
    private val tabDefaultColor = R.color.taboo_numbering_ball_default_text_color
    private val tabIndicatorColor: Int = com.kwon.taboo.uicore.R.color.taboo_blue_600

    private val ballDefaultColor = R.color.taboo_numbering_ball_default_background
    private val ballIndicatorColor = R.color.taboo_numbering_ball_default_background

    init {
        val typed = context.obtainStyledAttributes(attrs, R.styleable.TabooTabLayout)
        val isVisibilityNumbering = typed.getBoolean(R.styleable.TabooTabLayout_isVisibilityNumbering, false)
        val isVisibilityIcon = typed.getBoolean(R.styleable.TabooTabLayout_isVisibilityIcon, false)

        val tabFontFamily = typed.getResourceId(R.styleable.TabooTabLayout_tabFontFamily, com.kwon.taboo.uicore.R.font.font_pretendard_medium)

        val tabDefaultColor = typed.getColorStateList(R.styleable.TabooTabLayout_tabDefaultColor)
            ?: ContextCompat.getColorStateList(context, tabDefaultColor)
            ?: ColorStateList.valueOf(Color.BLACK)
        val tabIndicatorColor = typed.getColorStateList(R.styleable.TabooTabLayout_tabIndicatorColor)
            ?: ContextCompat.getColorStateList(context, tabIndicatorColor)
            ?: ColorStateList.valueOf(Color.BLUE)

        val ballDefaultColor = typed.getColorStateList(R.styleable.TabooTabLayout_ballDefaultColor)
            ?: ContextCompat.getColorStateList(context, ballDefaultColor)
            ?: ColorStateList.valueOf(Color.BLACK)
        val ballIndicatorColor = typed.getColorStateList(R.styleable.TabooTabLayout_ballIndicatorColor)
            ?: ContextCompat.getColorStateList(context, ballIndicatorColor)
            ?: ColorStateList.valueOf(Color.BLUE)

        typed.recycle()

        initTabLayout()

        isVisibilityNumbering(isVisibilityNumbering)
        isVisibilityIcon(isVisibilityIcon)

        setTabFontFamily(tabFontFamily)

        setTabColorInternal(tabDefaultColor.defaultColor, tabIndicatorColor.defaultColor)
        setBallColorInternal(ballDefaultColor.defaultColor, ballIndicatorColor.defaultColor)
    }

    private fun initTabLayout() {
        adapter = TabooTabAdapter()
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    /**
     * 마지막에 Tab을 추가합니다.
     *
     * @param tabBlock 추가할 Tab
     */
    fun addTab(tabBlock: TabooTabBlock) {
        addTab(tabBlock, (adapter as TabooTabAdapter).currentList.size)
    }

    /**
     * [position]에 Tab을 추가합니다.
     *
     * @param tabBlock 추가할 Tab
     * @param position 추가할 위치
     */
    fun addTab(tabBlock: TabooTabBlock, position: Int) {
        (adapter as TabooTabAdapter).submitList(
            (adapter as TabooTabAdapter).currentList.toMutableList().apply {
                add(position, tabBlock)
            }
        )

        (adapter as TabooTabAdapter).updateTab()
    }

    fun addTab(tabBlocks: List<TabooTabBlock>) {
        (adapter as TabooTabAdapter).submitList(
            (adapter as TabooTabAdapter).currentList.toMutableList().apply {
                addAll(tabBlocks)
            }
        )

        (adapter as TabooTabAdapter).updateTab()
    }

    /**
     * [position]에 Tab을 삭제합니다. [position]은 탭의 크기보다 작아야합니다.
     *
     * @param position 삭제할 Tab 위치
     */
    fun removeTab(position: Int) {
        (adapter as TabooTabAdapter).let { adapter ->
            // tab 크기보다 크거나 같은 position이 들어오면 에러 출력
            if (adapter.currentList.size <= position) {
                Log.e("TabooTabLayout", "removeTab: position is out of range")
                return
            }

            // Tab 삭제
            adapter.submitList(
                adapter.currentList.toMutableList().apply {
                    removeAt(position)
                }
            )
        }
    }

    fun setSelectedTabPosition(position: Int) {
        (adapter as TabooTabAdapter).setSelectedPosition(position)
    }

    fun isVisibilityNumbering(isVisibilityNumbering: Boolean) {
        (adapter as TabooTabAdapter).isVisibilityNumbering(isVisibilityNumbering)
    }

    fun isVisibilityNumbering(): Boolean {
        return (adapter as TabooTabAdapter).isVisibilityNumbering()
    }

    fun isVisibilityIcon(isVisibilityIcon: Boolean) {
        (adapter as TabooTabAdapter).isVisibilityIcon(isVisibilityIcon)
    }

    fun isVisibilityIcon(): Boolean {
        return (adapter as TabooTabAdapter).isVisibilityIcon()
    }

    fun setTabFontFamily(fontFamily: Int) {
        (adapter as TabooTabAdapter).setTabFontFamily(fontFamily)
    }

    /**
     * Tab 색상을 설정합니다.
     *
     * 만약 [defaultColor] 또는 [selectedColor]가 0이면 기본 색상이 적용됩니다.
     * 기본 색상은 아래와 같습니다.
     * - Default Color: [R.color.taboo_gray_700]
     * - Selected Color: [R.color.taboo_blue_600]
     *
     * 색상이 적용되는 부분은 아래와 같습니다.
     * - Tab Text
     * - Tab Icon
     * - Numbering Ball Text
     * - Indicator Line
     *
     * Ball 배경 색상을 변경하고 싶다면 [setBallColor]를 사용하세요.
     */
    private fun setTabColorInternal(defaultColor: Int, selectedColor: Int) {
        val colorStateList = ColorStateList(
            arrayOf(
                intArrayOf(android.R.attr.state_selected),      // Selected State
                intArrayOf(-android.R.attr.state_selected)      // Default State
            ),
            intArrayOf(
                selectedColor.takeIf { it != 0 } ?: tabIndicatorColor,      // Selected Color
                defaultColor.takeIf { it != 0 } ?: tabDefaultColor          // Default Color
            )
        )

        (adapter as TabooTabAdapter).setTabColorStateList(colorStateList)
    }

    /**
     * Tab Numbering Ball 배경 색상을 설정합니다.
     *
     * 만약 [defaultColor] 또는 [selectedColor]가 0이면 기본 색상이 적용됩니다.
     * 기본 색상은 아래와 같습니다.
     * - Default Color: [R.color.taboo_gray_400]
     * - Selected Color: [R.color.taboo_blue_100]
     *
     * @param defaultColor 기본 색상
     * @param selectedColor 선택 색상
     */
    private fun setBallColorInternal(defaultColor: Int, selectedColor: Int) {
        val colorStateList = ColorStateList(
            arrayOf(
                intArrayOf(android.R.attr.state_selected),      // Selected State
                intArrayOf(-android.R.attr.state_selected)      // Default State
            ),
            intArrayOf(
                selectedColor.takeIf { it != 0 } ?: ballIndicatorColor,      // Selected Color
                defaultColor.takeIf { it != 0 } ?: ballDefaultColor          // Default Color
            )
        )

        (adapter as TabooTabAdapter).setBallColorStateList(colorStateList)
    }

    private fun setBallColor(defaultColor: Int, selectedColor: Int) {
        setBallColorInternal(defaultColor, selectedColor)
    }
}