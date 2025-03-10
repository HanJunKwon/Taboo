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
    private val adapter = WheelPickerAdapter(context)
    private val layoutManager = LinearLayoutManager(context)

    init {
        val typed = context.obtainStyledAttributes(attrs, R.styleable.TabooWheelPicker)
        val textGravity = typed.getInt(R.styleable.TabooWheelPicker_textGravity, 1)

        typed.recycle()

        initWheelAdapter()

        setTextGravity(textGravity)
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

                    val (firstVisible, lastVisible) = getVisibleItemPosition()

                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        val selectedPosition = when {
                            lastVisible == adapter.itemCount - 1 && firstVisible == adapter.itemCount - 2 -> adapter.itemCount - 1
                            firstVisible == 0 && lastVisible == 1 -> 0
                            else -> (firstVisible + lastVisible) / 2
                        }

                        setSelectedPosition(selectedPosition)
                    }
                }
            })
        }

        setSelectedPosition(0)
    }

    fun setTextGravity(gravity: Int) {
        (binding.rvWheelPicker.adapter as WheelPickerAdapter).setTextGravity(gravity)
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

    private fun getVisibleItemPosition(): Pair<Int, Int> {
        val layoutManager = binding.rvWheelPicker.layoutManager as LinearLayoutManager

        val firstVisibleItemPosition = layoutManager.findFirstCompletelyVisibleItemPosition().takeIf { it != -1 } ?: layoutManager.findFirstVisibleItemPosition()
        val lastVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition().takeIf { it != -1 } ?: layoutManager.findLastVisibleItemPosition()

        return firstVisibleItemPosition to lastVisibleItemPosition
    }
}