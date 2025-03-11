package com.kwon.taboo.pickers.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.kwon.utils.calendar.ResourceUtils

class TabooWheelPickerItemDecoration(
    private var itemHeightPixel: Int
): ItemDecoration() {
    fun setItemHeight(itemHeightPixel: Int) {
        this.itemHeightPixel = itemHeightPixel
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = itemHeightPixel
        } else {
            outRect.top = 0
        }

        if (parent.getChildAdapterPosition(view) == parent.adapter!!.itemCount - 1) {
            outRect.bottom = itemHeightPixel
        } else {
            outRect.bottom = 0
        }
    }
}