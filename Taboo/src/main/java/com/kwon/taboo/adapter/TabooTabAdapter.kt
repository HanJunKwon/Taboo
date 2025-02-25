package com.kwon.taboo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.kwon.taboo.databinding.TabooTabBinding
import com.kwon.taboo.diffutils.TabooTabDiffCallback
import com.kwon.taboo.enums.PayLoad
import com.kwon.taboo.tabs.TabooTabBlock

class TabooTabAdapter: ListAdapter<TabooTabBlock, TabooTabAdapter.TabooTabViewHolder>(TabooTabDiffCallback()) {
    private var selectedTab: TabooTabBlock? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TabooTabViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = TabooTabBinding.inflate(inflater, parent, false)
        return TabooTabViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TabooTabViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onBindViewHolder(holder: TabooTabViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isNotEmpty()) {
            payloads.forEach { payload ->
                when (payload) {
                    PayLoad.SELECTION_CHANGED -> {
                        holder.updateSelected(selectedTab?.uuid == currentList[position].uuid)
                    }
                }
            }
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    fun setSelectedPosition(selectedPosition: Int) {
        val newSelectedTab = getItem(selectedPosition)

        // 이전 선택된 탭 비활성
        currentList
            .indexOfFirst { it.uuid == selectedTab?.uuid }
            .takeIf { it != -1 && it != selectedPosition }
            ?.let { prevSelectedPosition ->
                notifyItemChanged(prevSelectedPosition, PayLoad.SELECTION_CHANGED)
            }

        // 새로운 탭 저장
        this.selectedTab = newSelectedTab

        // 새로운 탭 선택
        notifyItemChanged(selectedPosition, PayLoad.SELECTION_CHANGED)
    }

    inner class TabooTabViewHolder(private val binding: TabooTabBinding): ViewHolder(binding.root) {
        fun bind(tab: TabooTabBlock) {
            binding.tvTabTitle.text = tab.tabName
            binding.tnbCount.text = tab.tabNumber.toString()

            updateSelected(currentList[adapterPosition].uuid == selectedTab?.uuid)

            binding.root.setOnClickListener {
                setSelectedPosition(adapterPosition)
            }
        }

        fun updateSelected(isSelected: Boolean) {
            binding.tvTabTitle.isSelected = isSelected
            binding.tnbCount.isActivated = isSelected
        }
    }
}
