package com.kwon.taboo.adapter

import android.content.Context
import android.content.res.ColorStateList
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

    private var tabColorStateList: ColorStateList? = null
    private var ballColorStateList: ColorStateList? = null

    private var isVisibilityNumbering = false
    private var isVisibilityIcon = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TabooTabViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = TabooTabBinding.inflate(inflater, parent, false)
        return TabooTabViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TabooTabViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onBindViewHolder(holder: TabooTabViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
            return
        }

        payloads.forEach { payload ->
            when (payload) {
                // 선택된 탭 변경
                PayLoad.SELECTION_CHANGED -> {
                    holder.updateSelected(
                        isSelected = selectedTab?.uuid == currentList[position].uuid
                    )
                }
                // 숫자 표시 여부 변경
                PayLoad.NUMBERING_VISIBILITY_CHANGED -> {
                    holder.updateNumberingVisibility(isVisibilityNumbering)
                }
                // 아이콘 표시 여부 변경
                PayLoad.ICON_VISIBILITY_CHANGED -> {
                    holder.updateIconVisibility(isVisibilityIcon)
                }
                // 탭 색상 변경
                PayLoad.TAB_COLOR_CHANGED -> {
                    holder.updateTabColor()
                }
                // Ball 색상 변경
                PayLoad.BALL_COLOR_CHANGED -> {
                    holder.updateBallColor()
                }
            }
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

    /**
     * 탭 아이콘을 표시할지 여부를 설정합니다.
     *
     * @param isVisibilityIcon `true`: 아이콘 표시, `false`: 아이콘 미표시
     */
    fun isVisibilityIcon(isVisibilityIcon: Boolean) {
        this.isVisibilityIcon = isVisibilityIcon

        notifyItemRangeChanged(0, itemCount, PayLoad.ICON_VISIBILITY_CHANGED)
    }

    /**
     * 아이콘 표시 여부를 반환합니다.
     *
     * @return `true`: 아이콘 표시, `false`: 아이콘 미표시
     */
    fun isVisibilityIcon(): Boolean {
        return isVisibilityIcon
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

    fun setTabColorStateList(tabColorStateList: ColorStateList) {
        this.tabColorStateList = tabColorStateList

        notifyItemRangeChanged(0, itemCount, PayLoad.TAB_COLOR_CHANGED)
    }

    fun setBallColorStateList(ballColorStateList: ColorStateList) {
        this.ballColorStateList = ballColorStateList

        notifyItemRangeChanged(0, itemCount, PayLoad.BALL_COLOR_CHANGED)
    }

    /**
     * 탭 뷰홀더
     */
    inner class TabooTabViewHolder(private val binding: TabooTabBinding): ViewHolder(binding.root) {
        /**
         * 탭 데이터를 뷰에 바인딩.
         */
        fun bind(tab: TabooTabBlock) {
            binding.tvTabTitle.text = tab.tabName
            binding.tnbCount.text = tab.tabNumber.toString()
            if (tab.tabIcon != 0) {
                binding.ivTabIcon.setImageDrawable(ContextCompat.getDrawable(binding.root.context, tab.tabIcon))
            }

            updateNumberingVisibility(isVisibilityNumbering)
            updateIconVisibility(isVisibilityIcon)
            updateTabColor()
            updateBallColor()
            updateSelected(currentList[adapterPosition].uuid == selectedTab?.uuid)

            binding.root.setOnClickListener {
                setSelectedPosition(adapterPosition)
            }
        }

        /**
         * 선택 여부에 따라 탭 업데이트.
         *
         * 선택된 탭은 `TabooBlue02` 색상으로 표시되고, 선택되지 않은 탭은 `TabooBlack03` 색상으로 표시됩니다.
         *
         * @param isSelected `true`: 선택된 탭, `false`: 선택되지 않은 탭
         */
        fun updateSelected(isSelected: Boolean) {
            binding.tvTabTitle.isSelected = isSelected
            binding.tnbCount.isSelected = isSelected
            binding.ivTabIcon.isSelected = isSelected
        }

        /**
         * **숫자** 표시 여부에 따라 숫자 `Visibility` 업데이트.
         *
         * @param isVisibilityNumbering `true`: 숫자 표시, `false`: 숫자 미표시
         */
        fun updateNumberingVisibility(isVisibilityNumbering: Boolean) {
            binding.tnbCount.visibility = if (isVisibilityNumbering) {
                android.view.View.VISIBLE
            } else {
                android.view.View.GONE
            }
        }

        /**
         * **아이콘** 표시 여부에 따라 아이콘 `Visibility` 업데이트.
         *
         * @param isVisibilityIcon `true`: 아이콘 표시, `false`: 아이콘 미표시
         */
        fun updateIconVisibility(isVisibilityIcon: Boolean) {
            binding.ivTabIcon.visibility = if (isVisibilityIcon && binding.ivTabIcon.drawable != null) {
                android.view.View.VISIBLE
            } else {
                android.view.View.GONE
            }
        }

        fun updateTabColor() {
            tabColorStateList?.let { colorStateList ->
                binding.tvTabTitle.setTextColor(colorStateList)
                binding.tnbCount.setTextColor(colorStateList)
                binding.ivTabIcon.drawable?.setTintList(colorStateList)
            }
        }

        fun updateBallColor() {
            ballColorStateList?.let { colorStateList ->
                binding.tnbCount.setBallColor(colorStateList)
            }
        }
    }
}
