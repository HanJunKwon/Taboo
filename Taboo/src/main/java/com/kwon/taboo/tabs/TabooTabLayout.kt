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

    private var tabDefaultColor = R.color.taboo_black_03
    private var tabIndicatorColor: Int = R.color.taboo_blue_02

    init {
        val typed = context.obtainStyledAttributes(attrs, R.styleable.TabooTabLayout)
        val isVisibilityNumbering = typed.getBoolean(R.styleable.TabooTabLayout_isVisibilityNumbering, false)
        val isVisibilityIcon = typed.getBoolean(R.styleable.TabooTabLayout_isVisibilityIcon, false)
        val tabDefaultColor = typed.getColorStateList(R.styleable.TabooTabLayout_tabDefaultColor)
            ?: ContextCompat.getColorStateList(context, tabDefaultColor)
            ?: ColorStateList.valueOf(Color.BLACK)
        val tabIndicatorColor = typed.getColorStateList(R.styleable.TabooTabLayout_tabIndicatorColor)
            ?: ContextCompat.getColorStateList(context, tabIndicatorColor)
            ?: ColorStateList.valueOf(Color.BLUE)

        typed.recycle()

        initTabLayout()

        isVisibilityNumbering(isVisibilityNumbering)
        isVisibilityIcon(isVisibilityIcon)

        setTabColorInternal(tabDefaultColor.defaultColor, tabIndicatorColor.defaultColor)
    }

    private fun initTabLayout() {
        adapter = TabooTabAdapter().apply {
            submitList(
                listOf(
                    TabooTabBlock(tabName = "Tab 1", tabNumber = 1),
                    TabooTabBlock(tabName = "Tab 2", tabNumber = 2),
                    TabooTabBlock(tabName = "Tab 3", tabNumber = 3)
                )
            )
        }
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    fun addTab(tabBlock: TabooTabBlock) {
        addTab(tabBlock, (adapter as TabooTabAdapter).currentList.size)
    }

    fun addTab(tabBlock: TabooTabBlock, position: Int) {
        (adapter as TabooTabAdapter).submitList(
            (adapter as TabooTabAdapter).currentList.toMutableList().apply {
                add(position, tabBlock)
            }
        )
    }

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
}