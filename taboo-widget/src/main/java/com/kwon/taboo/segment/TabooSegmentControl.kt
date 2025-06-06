package com.kwon.taboo.segment

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kwon.taboo.R
import com.kwon.taboo.adapter.TabooSegmentControlAdapter
import com.kwon.taboo.segment.decoration.TabooSegmentDecoration

class TabooSegmentControl(
    context: Context,
    attrs: AttributeSet
) : RecyclerView(context, attrs) {

    init {
        val typed = context.obtainStyledAttributes(attrs, R.styleable.TabooSegmentControl)
        val segmentType = typed.getInt(R.styleable.TabooSegmentControl_segmentType, 0)
        typed.recycle()

        initAdapter(segmentType)
    }

    private fun initAdapter(@TabooSegmentControlAdapter.SegmentType segmentType: Int) {
        adapter = TabooSegmentControlAdapter(segmentType)
        addItemDecoration(TabooSegmentDecoration())
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    fun setItems(items: List<String>) {
        (adapter as TabooSegmentControlAdapter).submitList(items)
    }

    fun setOnItemClickListener(listener: (Int) -> Unit) {
        (adapter as TabooSegmentControlAdapter).setOnItemClickListener(listener)
    }

    fun setOnSelectedItemChangedListener(listener: (Int) -> Unit) {
        (adapter as TabooSegmentControlAdapter).setOnItemSelectedChangedListener(listener)
    }

    fun setSelectedItem(position: Int) {
        (adapter as TabooSegmentControlAdapter).setSelectedPosition(position)
    }
}