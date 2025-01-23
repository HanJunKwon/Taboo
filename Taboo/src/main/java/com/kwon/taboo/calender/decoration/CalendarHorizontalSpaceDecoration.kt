package com.kwon.taboo.calender.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class CalendarHorizontalSpaceDecoration(private var space: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)

        if (position == state.itemCount)
            return

        // 매달 1일은 앞에 공백 추가
        if (position == 0) {
            outRect.left = space
        }

        outRect.right = space
    }
}