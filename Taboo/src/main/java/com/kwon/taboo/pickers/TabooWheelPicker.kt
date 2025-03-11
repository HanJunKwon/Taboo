package com.kwon.taboo.pickers

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.InsetDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
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

    private var itemDecoration = TabooWheelPickerItemDecoration(0)
    private var itemHeightPixel: Int = resources.getDimensionPixelSize(R.dimen.taboo_wheel_picker_item_default_height)

    @ColorInt private var selectionStrokeColor = ContextCompat.getColor(context, R.color.taboo_gray_03)

    init {
        val typed = context.obtainStyledAttributes(attrs, R.styleable.TabooWheelPicker)
        val textGravity = typed.getInt(R.styleable.TabooWheelPicker_textGravity, 1)
        val itemHeight = typed.getDimension(
            R.styleable.TabooWheelPicker_itemHeight,
            resources.getDimension(R.dimen.taboo_wheel_picker_item_default_height)
        )
        val selectionStrokeColor = typed.getColor(
            R.styleable.TabooWheelPicker_selectionStrokeColor,
            ContextCompat.getColor(context, R.color.taboo_gray_03)
        )

        typed.recycle()

        initWheelAdapter()

        setTextGravity(textGravity)
        setItemHeight(itemHeight)
        setSelectionStrokeColorInternal(selectionStrokeColor)

        initTabooWheelPicker()
    }

    private fun initTabooWheelPicker() {
        updateSelectionStrokeColor()
    }

    private fun initWheelAdapter() {
        binding.rvWheelPicker.apply {
            adapter = WheelPickerAdapter(context)
            layoutManager = LinearLayoutManager(context).apply {
                orientation = LinearLayoutManager.VERTICAL
                isItemPrefetchEnabled = false
            }

            addItemDecoration(itemDecoration)

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

    /**
     * 아이템의 높이를 dp로 설정.
     * @param height dp
     */
    fun setItemHeight(height: Float) {
        setItemHeight(ResourceUtils.dpToPx(context, height).toInt())
    }

    /**
     * 아이템의 높이를 pixel로 설정.
     * @param height pixel
     */
    fun setItemHeight(height: Int) {
        itemHeightPixel = height

        updateItemHeight()
    }

    /**
     * 아이템의 높이를 변경한 경우 호출.
     * - RecyclerView 의 아이템 높이 변경
     * - RecyclerView 첫 번째 아이템의 Top, 마지막 아이템의 Bottom offset 변경
     * - 선택된 아이템의 중앙 Offset 변경
     */
    private fun updateItemHeight() {
        // RecyclerView 의 높이 변경
        binding.rvWheelPicker.layoutParams = binding.rvWheelPicker.layoutParams.apply {
            height = itemHeightPixel * 3
        }

        // 목록의 아이템 높이 변경
        (binding.rvWheelPicker.adapter as WheelPickerAdapter).setItemHeight(itemHeightPixel)

        // 선택된 아이템의 중앙 Offset 변경
        binding.clWheelPicker.layoutParams = binding.clWheelPicker.layoutParams.apply {
            height = itemHeightPixel
        }

        // 최상위, 최하위 아이템의 Top, Bottom offset 변경
        binding.rvWheelPicker.getItemDecorationAt(0).let {
            (it as TabooWheelPickerItemDecoration).setItemHeight(itemHeightPixel)
            binding.rvWheelPicker.invalidateItemDecorations()
        }
    }

    fun setItems(items: List<String>) {
        (binding.rvWheelPicker.adapter as WheelPickerAdapter).submitList(items)
    }

    fun setSelectedPosition(position: Int) {
        val adapter = binding.rvWheelPicker.adapter as WheelPickerAdapter
        val layoutManager = binding.rvWheelPicker.layoutManager as LinearLayoutManager

        layoutManager.scrollToPositionWithOffset(position, itemHeightPixel)
        adapter.selectedPosition(position)
    }

    private fun setSelectionStrokeColorInternal(color: Int) {
        selectionStrokeColor = color
    }

    fun setSelectionStrokeColor(@ColorRes color: Int) {
        setSelectionStrokeColorInternal(ContextCompat.getColor(context, color))

        updateSelectionStrokeColor()
    }

    private fun updateSelectionStrokeColor() {
        binding.clWheelPicker.background = createSelectionBox()
    }

    private fun createSelectionBox(): LayerDrawable{
        val gradientDecoration = GradientDrawable().apply {
            setStroke(1, selectionStrokeColor)
        }

        val insetDrawable = InsetDrawable(gradientDecoration, -5, 0, -5, 0)

        return LayerDrawable(arrayOf(insetDrawable))
    }

    private fun getVisibleItemPosition(): Pair<Int, Int> {
        val layoutManager = binding.rvWheelPicker.layoutManager as LinearLayoutManager

        val firstVisibleItemPosition = layoutManager.findFirstCompletelyVisibleItemPosition().takeIf { it != -1 } ?: layoutManager.findFirstVisibleItemPosition()
        val lastVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition().takeIf { it != -1 } ?: layoutManager.findLastVisibleItemPosition()

        return firstVisibleItemPosition to lastVisibleItemPosition
    }
}