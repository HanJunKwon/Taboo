package com.kwon.taboo.pickers

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kwon.taboo.R
import com.kwon.taboo.databinding.TabooWheelPickerBinding
import com.kwon.taboo.pickers.adapter.WheelPickerAdapter
import com.kwon.taboo.pickers.decoration.TabooWheelPickerItemDecoration
import com.kwon.utils.calendar.ResourceUtils

class TabooWheelPicker(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {
    private val binding = TabooWheelPickerBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        val typed = context.obtainStyledAttributes(attrs, R.styleable.TabooWheelPicker)
        typed.recycle()

        initWheelAdapter()

        setItems((0..100).map { it.toString() })
    }

    private fun initWheelAdapter() {
        binding.rvWheelPicker.apply {
            adapter = WheelPickerAdapter(context)
            layoutManager = LinearLayoutManager(context).apply {
                orientation = LinearLayoutManager.VERTICAL
                isItemPrefetchEnabled = false
            }

            addItemDecoration(TabooWheelPickerItemDecoration())

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    val adapter = binding.rvWheelPicker.adapter as WheelPickerAdapter
                    val layoutManager = binding.rvWheelPicker.layoutManager as LinearLayoutManager

                    val firstVisibleItemPosition = layoutManager.findFirstCompletelyVisibleItemPosition().takeIf { it != -1 } ?: layoutManager.findFirstVisibleItemPosition()
                    val lastVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition().takeIf { it != -1 } ?: layoutManager.findLastVisibleItemPosition()

                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        val selectedPosition = if (lastVisibleItemPosition == adapter.itemCount - 1 && firstVisibleItemPosition == adapter.itemCount - 2) {
                            adapter.itemCount - 1
                        } else if (firstVisibleItemPosition == 0 && lastVisibleItemPosition == 1) {
                            0
                        } else {
                            Math.floorDiv(firstVisibleItemPosition + lastVisibleItemPosition, 2)
                        }

                        setSelectedPosition(selectedPosition)
                    }
                }
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                }
            })
        }

        setSelectedPosition(0)
    }

    fun setItems(items: List<String>) {
        (binding.rvWheelPicker.adapter as WheelPickerAdapter).submitList(items)
    }

    fun setSelectedPosition(position: Int) {
        val adapter = binding.rvWheelPicker.adapter as WheelPickerAdapter
        val layoutManager = binding.rvWheelPicker.layoutManager as LinearLayoutManager

        layoutManager.scrollToPositionWithOffset(position, ResourceUtils.dpToPx(context, 32f).toInt())
        adapter.selectedPosition(position)
    }

}