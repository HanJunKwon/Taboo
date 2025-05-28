package com.kwon.taboo.pickers

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.InsetDrawable
import android.graphics.drawable.LayerDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kwon.taboo.R
import com.kwon.taboo.pickers.adapter.WheelPickerAdapter
import com.kwon.taboo.pickers.decoration.TabooWheelPickerItemDecoration
import com.kwon.taboo.uicore.util.ResourceUtils

class TabooWheelPicker(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {
    private val rootView = LayoutInflater.from(context).inflate(R.layout.taboo_wheel_picker, this, true)

    private var itemDecoration = TabooWheelPickerItemDecoration(0)
    private var itemHeightPixel: Int = resources.getDimensionPixelSize(R.dimen.taboo_wheel_picker_item_default_height)

    @ColorInt private var selectionStrokeColor = ContextCompat.getColor(context, com.kwon.taboo.uicore.R.color.taboo_gray_200)
    private var selectionStrokeWidth = 1f

    init {
        val typed = context.obtainStyledAttributes(attrs, R.styleable.TabooWheelPicker)
        val textGravity = typed.getInt(R.styleable.TabooWheelPicker_textGravity, 1)
        val itemHeight = typed.getDimension(
            R.styleable.TabooWheelPicker_itemHeight,
            resources.getDimension(R.dimen.taboo_wheel_picker_item_default_height)
        )
        val selectionStrokeColor = typed.getColor(
            R.styleable.TabooWheelPicker_selectionStrokeColor,
            ContextCompat.getColor(context, com.kwon.taboo.uicore.R.color.taboo_gray_200)
        )
        val selectionStrokeWidth = typed.getDimension(
            R.styleable.TabooWheelPicker_selectionStrokeWidth,
            resources.getDimension(R.dimen.taboo_wheel_picker_selection_box_default_stroke_width)
        )

        typed.recycle()

        initWheelAdapter()

        setTextGravity(textGravity)
        setItemHeight(itemHeight)
        setSelectionStrokeColorInternal(selectionStrokeColor)
        setSelectionStrokeWidthInternal(selectionStrokeWidth)

        initTabooWheelPicker()
    }

    private fun initTabooWheelPicker() {
        updateSelectionBox()
    }

    private fun initWheelAdapter() {
        rootView.findViewById<RecyclerView>(R.id.rv_wheel_picker).apply {
            adapter = WheelPickerAdapter(context)
            layoutManager = LinearLayoutManager(context).apply {
                orientation = LinearLayoutManager.VERTICAL
                isItemPrefetchEnabled = false
            }

            addItemDecoration(itemDecoration)

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    val adapter = this@TabooWheelPicker.getAdapter()

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

    fun getAdapter(): WheelPickerAdapter {
        return rootView.findViewById<RecyclerView>(R.id.rv_wheel_picker).adapter as WheelPickerAdapter
    }

    fun getLayoutManager(): LinearLayoutManager {
        return rootView.findViewById<RecyclerView>(R.id.rv_wheel_picker).layoutManager as LinearLayoutManager
    }

    fun setTextGravity(gravity: Int) {
        getAdapter().setTextGravity(gravity)
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
        val wheelPickerView = rootView.findViewById<RecyclerView>(R.id.rv_wheel_picker)
        val clPickerView = rootView.findViewById<ConstraintLayout>(R.id.cl_wheel_picker)

        // 3개의 아이템이 보이도록 설정
        wheelPickerView.layoutParams = wheelPickerView.layoutParams.apply {
            height = itemHeightPixel * 3
        }

        // 목록의 아이템 높이 변경
        getAdapter().setItemHeight(itemHeightPixel)

        // 선택된 아이템의 중앙 Offset 변경
        clPickerView.layoutParams = clPickerView.layoutParams.apply {
            height = itemHeightPixel
        }

        // 최상위, 최하위 아이템의 Top, Bottom offset 변경
        wheelPickerView.getItemDecorationAt(0).let {
            getAdapter().setItemHeight(itemHeightPixel)
            wheelPickerView.invalidateItemDecorations()
        }
    }

    fun setItems(items: List<String>) {
        getAdapter().submitList(items)
    }

    fun setSelectedPosition(position: Int) {
        getLayoutManager().scrollToPositionWithOffset(position, itemHeightPixel)
        getAdapter().selectedPosition(position)
    }

    private fun setSelectionStrokeColorInternal(color: Int) {
        selectionStrokeColor = color
    }

    fun setSelectionStrokeColor(@ColorRes color: Int) {
        setSelectionStrokeColorInternal(ContextCompat.getColor(context, color))

        updateSelectionBox()
    }


    private fun setSelectionStrokeWidthInternal(width: Float) {
        selectionStrokeWidth = width
    }

    fun setSelectionStrokeWidth(width: Float) {
        setSelectionStrokeWidthInternal(width)

        updateSelectionBox()
    }

    private fun updateSelectionBox() {
        rootView.findViewById<ConstraintLayout>(R.id.cl_wheel_picker).background = createSelectionBox()
    }

    /**
     * 선택 영역을 그리기 위한 Drawable 생성.
     *
     * Drawable은 아래와 같은 구조로 생성된다.
     *
     *
     * @return [LayerDrawable]
     */
    private fun createSelectionBox(): LayerDrawable{
        val gradientDecoration = GradientDrawable().apply {
            setStroke(
                ResourceUtils.dpToPx(context, selectionStrokeWidth),
                selectionStrokeColor
            )
        }

        val horizontalOffset = -(ResourceUtils.dpToPx(context, 5f) * selectionStrokeWidth).toInt()

        val insetDrawable = InsetDrawable(gradientDecoration, horizontalOffset, 0, horizontalOffset, 0)

        return LayerDrawable(arrayOf(insetDrawable))
    }

    private fun getVisibleItemPosition(): Pair<Int, Int> {
        val layoutManager = getLayoutManager()

        val firstVisibleItemPosition = layoutManager.findFirstCompletelyVisibleItemPosition().takeIf { it != -1 } ?: layoutManager.findFirstVisibleItemPosition()
        val lastVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition().takeIf { it != -1 } ?: layoutManager.findLastVisibleItemPosition()

        return firstVisibleItemPosition to lastVisibleItemPosition
    }
}