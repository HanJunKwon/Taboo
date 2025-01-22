package com.kwon.taboo.calender.scroller

import android.content.Context
import androidx.recyclerview.widget.LinearSmoothScroller

class CenterSmoothScroller(context: Context) : LinearSmoothScroller(context) {
    override fun calculateDtToFit(
        viewStart: Int,
        viewEnd: Int,
        boxStart: Int,
        boxEnd: Int,
        snapPreference: Int
    ): Int {
        // 중앙에 배치되도록 계산
        return (boxStart + boxEnd - viewStart - viewEnd) / 2
    }
}