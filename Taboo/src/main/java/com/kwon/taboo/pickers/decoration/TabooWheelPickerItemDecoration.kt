package com.kwon.taboo.pickers.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.kwon.utils.calendar.ResourceUtils

class TabooWheelPickerItemDecoration: ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = ResourceUtils.dpToPx(parent.context, 32f).toInt()
        } else {
            outRect.top = 0
        }

        if (parent.getChildAdapterPosition(view) == parent.adapter!!.itemCount - 1) {
            outRect.bottom = ResourceUtils.dpToPx(parent.context, 32f).toInt()
        } else {
            outRect.bottom = 0
        }
    }
}