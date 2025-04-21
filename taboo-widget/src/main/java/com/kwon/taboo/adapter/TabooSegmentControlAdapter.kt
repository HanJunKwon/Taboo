package com.kwon.taboo.adapter

import android.util.Log
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.NO_POSITION
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.kwon.taboo.R
import com.kwon.taboo.diffutils.TabooSegmentDiffCallback
import com.kwon.taboo.enums.PayLoad
import com.kwon.taboo.uicore.util.ResourceUtils

class TabooSegmentControlAdapter : ListAdapter<String, TabooSegmentControlAdapter.TabooSegmentButtonViewHolder>(TabooSegmentDiffCallback()) {
    private var selectedIndex = NO_POSITION

    private var onItemClickListener: ((Int) -> Unit)? = null
    private var onItemSelectedChangedListener: ((Int) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TabooSegmentControlAdapter.TabooSegmentButtonViewHolder {
        val textView = TextView(parent.context)
        return TabooSegmentButtonViewHolder(textView)
    }

    override fun onBindViewHolder(
        holder: TabooSegmentControlAdapter.TabooSegmentButtonViewHolder,
        position: Int
    ) {
        holder.bind()
    }

    override fun onBindViewHolder(
        holder: TabooSegmentButtonViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
            return
        }

        payloads.forEach { payload ->
            when (payload) {
                PayLoad.SELECTION_CHANGED -> {
                    holder.updateSelected(selectedIndex == position)
                }
            }
        }
    }

    fun setSelectedPosition(position: Int) {
        if (position < 0) {
            Log.e("TabooSegmentControlAdapter", "Invalid position: $position")
            return
        }

        if (position >= itemCount) {
            Log.e("TabooSegmentControlAdapter", "Invalid position: $position")
            return
        }

        if (selectedIndex != NO_POSITION) {
            notifyItemChanged(selectedIndex, PayLoad.SELECTION_CHANGED)
        }

        selectedIndex = position
        notifyItemChanged(selectedIndex, PayLoad.SELECTION_CHANGED)

        onItemSelectedChangedListener?.invoke(selectedIndex)
    }

    fun setOnItemClickListener(listener: (Int) -> Unit) {
        onItemClickListener = listener
    }

    fun setOnItemSelectedChangedListener(listener: (Int) -> Unit) {
        onItemSelectedChangedListener = listener
    }

    inner class TabooSegmentButtonViewHolder(private val binding: TextView): ViewHolder(binding) {
        fun bind() {
            binding.text = getItem(adapterPosition)
            binding.textSize = 12f
            binding.typeface = ResourcesCompat.getFont(binding.context, com.kwon.taboo.uicore.R.font.font_pretendard_regular)
            binding.setTextColor(ContextCompat.getColorStateList(binding.context, R.color.selector_taboo_segement_control_button_text_color))
            binding.background = ContextCompat.getDrawable(binding.context, R.drawable.selector_taboo_segment_control_background)

            val paddingVertical = ResourceUtils.dpToPx(binding.context, 7f)
            val paddingHorizontal = ResourceUtils.dpToPx(binding.context, 15f)
            binding.setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical)

            updateSelected(selectedIndex == adapterPosition)

            binding.setOnClickListener {
                onItemClickListener?.invoke(adapterPosition)

                setSelectedPosition(adapterPosition)
            }
        }

        fun updateSelected(isSelected: Boolean) {
            binding.isSelected = isSelected
        }
    }

}