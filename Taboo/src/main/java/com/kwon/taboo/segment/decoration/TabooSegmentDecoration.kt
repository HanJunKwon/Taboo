package com.kwon.taboo.segment.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.kwon.utils.calendar.ResourceUtils

class TabooSegmentDecoration: RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)

        if (position == 0) {
            outRect.left = ResourceUtils.dpToPx(view.context, 10f).toInt()
        }

        outRect.right = ResourceUtils.dpToPx(view.context, 10f).toInt()
    }
}