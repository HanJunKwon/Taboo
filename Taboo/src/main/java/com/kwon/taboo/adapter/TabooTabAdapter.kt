package com.kwon.taboo.adapter

import android.content.Context
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.kwon.taboo.R
import com.kwon.taboo.databinding.TabooTabBinding
import com.kwon.taboo.diffutils.TabooTabDiffCallback
import com.kwon.taboo.enums.PayLoad
import com.kwon.taboo.tabs.TabooTabBlock

class TabooTabAdapter: ListAdapter<TabooTabBlock, TabooTabAdapter.TabooTabViewHolder>(TabooTabDiffCallback()) {
    private var selectedTab: TabooTabBlock? = null
    private var isVisibilityNumbering = false

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
                        holder.updateSelected(holder.itemView.context,selectedTab?.uuid == currentList[position].uuid)
                    }
                    PayLoad.NUMBERING_VISIBILITY_CHANGED -> {
                        holder.updateNumberingVisibility(isVisibilityNumbering)
                    }
                }
            }
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    /**
     * 탭 이름 오른쪽에 `TabooNumberingBall`을 사용하여 표시하는 숫자를 표시할지 여부를 설정합니다.
     *
     * @param isVisibilityNumbering `true`: 숫자 표시, `false`: 숫자 미표시
     */
    fun isVisibilityNumbering(isVisibilityNumbering: Boolean) {
        this.isVisibilityNumbering = isVisibilityNumbering

        // Numbering Visibility 변경
        notifyItemRangeChanged(0, itemCount, PayLoad.NUMBERING_VISIBILITY_CHANGED)
    }

    /**
     * 숫자 표시 여부를 반환합니다.
     *
     * @return `true`: 숫자 표시, `false`: 숫자 미표시
     */
    fun isVisibilityNumbering(): Boolean {
        return isVisibilityNumbering
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
            if (tab.tabIcon != 0) {
                binding.ivTabIcon.setImageDrawable(ContextCompat.getDrawable(binding.root.context, tab.tabIcon))
            }

            updateNumberingVisibility(isVisibilityNumbering)
            updateSelected(binding.root.context, currentList[adapterPosition].uuid == selectedTab?.uuid)

            binding.root.setOnClickListener {
                setSelectedPosition(adapterPosition)
            }
        }

        /**
         * 선택 여부에 따라 탭 업데이트.
         */
        fun updateSelected(context: Context, isSelected: Boolean) {
            binding.tvTabTitle.isSelected = isSelected
            binding.tnbCount.isActivated = isSelected
            binding.ivTabIcon.setColorFilter(
                ContextCompat.getColor(context, if (isSelected) R.color.taboo_blue_02 else R.color.taboo_black_03),
                PorterDuff.Mode.SRC_IN
            )
        }

        /**
         * 숫자 표시 여부에 따라 숫자 `Visibility` 업데이트.
         */
        fun updateNumberingVisibility(isVisibilityNumbering: Boolean) {
            binding.tnbCount.visibility = if (isVisibilityNumbering) {
                android.view.View.VISIBLE
            } else {
                android.view.View.GONE
            }
        }
    }
}
