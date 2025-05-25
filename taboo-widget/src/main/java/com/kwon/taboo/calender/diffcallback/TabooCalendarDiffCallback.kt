package com.kwon.taboo.calender.diffcallback

import androidx.recyclerview.widget.DiffUtil
import com.kwon.taboo.calender.CalendarBlock

class TabooCalendarDiffCallback: DiffUtil.ItemCallback<CalendarBlock>() {
    override fun areItemsTheSame(oldItem: CalendarBlock, newItem: CalendarBlock): Boolean {
        return oldItem.timestamp == newItem.timestamp
    }

    override fun areContentsTheSame(oldItem: CalendarBlock, newItem: CalendarBlock): Boolean {
        return oldItem == newItem
    }
}