package com.kwon.taboo.tabs

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kwon.taboo.R
import com.kwon.taboo.adapter.TabooTabAdapter

class TabooTabLayout(
    context: Context,
    attrs: AttributeSet
) : RecyclerView(context, attrs) {
    init {
        val typed = context.obtainStyledAttributes(attrs, R.styleable.TabooTabLayout)
        val isVisibilityNumbering = typed.getBoolean(R.styleable.TabooTabLayout_isVisibilityNumbering, false)

        typed.recycle()

        initTabLayout()

        isVisibilityNumbering(isVisibilityNumbering)
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
}