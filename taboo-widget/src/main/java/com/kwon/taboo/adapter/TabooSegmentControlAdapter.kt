package com.kwon.taboo.adapter

import android.util.Log
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.IntDef
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.NO_POSITION
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.kwon.taboo.R
import com.kwon.taboo.diffutils.TabooSegmentDiffCallback
import com.kwon.taboo.enums.PayLoad
import com.kwon.taboo.uicore.util.ResourceUtils

class TabooSegmentControlAdapter(@SegmentType private val segmentType: Int = SEGMENT_TYPE_SOLID)
    : ListAdapter<String, TabooSegmentControlAdapter.TabooSegmentButtonViewHolder>(TabooSegmentDiffCallback()) {
    private var selectedIndex = NO_POSITION

    private var textColorState: Int? = null
    private var background: Int? = null

    private var onItemClickListener: ((Int) -> Unit)? = null
    private var onItemSelectedChangedListener: ((Int) -> Unit)? = null


    init {
        when (segmentType) {
            SEGMENT_TYPE_SOLID -> {
                textColorState = R.color.selector_taboo_segment_control_solid_type_text_color
                background = R.drawable.selector_taboo_segment_control_solid_type_background
            }
            SEGMENT_TYPE_OUTLINE -> {
                textColorState = R.color.selector_taboo_segment_control_outline_type_text_color
                background = R.drawable.selector_taboo_segment_control_outline_type_background
            }
            else -> {
                throw IllegalArgumentException("Invalid segment type: $segmentType")
            }
        }
    }

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
            binding.setTextColor(ContextCompat.getColorStateList(binding.context, textColorState!!))
            binding.background = ContextCompat.getDrawable(binding.context, background!!)

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

    @IntDef(
        SEGMENT_TYPE_SOLID,
        SEGMENT_TYPE_OUTLINE
    )
    @Retention(AnnotationRetention.SOURCE)
    annotation class SegmentType

    companion object {
        private const val SEGMENT_TYPE_SOLID = 0
        private const val SEGMENT_TYPE_OUTLINE = 1
    }

}