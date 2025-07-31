package com.kwon.taboo.tabs.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.kwon.taboo.uicore.util.ResourceUtils

class TabooTabDecoration(var tabSpace: Float = 0f): RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)

        if (position != 0) {
            outRect.left = ResourceUtils.dpToPx(view.context, tabSpace).toInt()
        }
    }
}