package com.kwon.taboo.diffutils

import androidx.recyclerview.widget.DiffUtil
import com.kwon.taboo.tabs.TabooTabBlock

class TabooTabDiffCallback : DiffUtil.ItemCallback<TabooTabBlock>() {
    override fun areItemsTheSame(oldItem: TabooTabBlock, newItem: TabooTabBlock): Boolean {
        return oldItem.uuid == newItem.uuid
    }

    override fun areContentsTheSame(oldItem: TabooTabBlock, newItem: TabooTabBlock): Boolean {
        return oldItem == newItem
    }
}