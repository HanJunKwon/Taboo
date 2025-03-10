package com.kwon.taboo.pickers.adapter

import android.content.Context
import android.util.Log
import android.view.Gravity.CENTER_VERTICAL
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.NO_POSITION
import androidx.recyclerview.widget.RecyclerView.TEXT_ALIGNMENT_CENTER
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.kwon.taboo.R
import com.kwon.taboo.diffutils.TabooWheelPickerDiffCallback
import com.kwon.taboo.enums.PayLoad
import com.kwon.utils.calendar.ResourceUtils

class WheelPickerAdapter(
    private val context: Context,
): ListAdapter<String, WheelPickerAdapter.WheelPickerViewHolder>(TabooWheelPickerDiffCallback()) {

    private var selectedPosition = NO_POSITION

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WheelPickerViewHolder {
        val textView = createPickerTextView()
        return WheelPickerViewHolder(textView)
    }

    override fun onBindViewHolder(holder: WheelPickerViewHolder, position: Int) {
        holder.bind()
    }

    override fun onBindViewHolder(
        holder: WheelPickerViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else{
            payloads.forEach { payload ->
                when (payload) {
                    PayLoad.SELECTION_CHANGED -> {
                        holder.setSelected(position == selectedPosition)
                    }
                }
            }
        }
    }

    private fun createPickerTextView(): TextView {
        return TextView(context).apply {
            layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ResourceUtils.dpToPx(context, 32f).toInt())
            textAlignment = TEXT_ALIGNMENT_CENTER
            gravity = TEXT_ALIGNMENT_CENTER
            setTextColor(ContextCompat.getColor(context, R.color.taboo_black_01))
        }
    }

    fun selectedPosition (position: Int) {
        if (selectedPosition == position) return
        selectedPosition = position
        notifyItemRangeChanged(0, itemCount, PayLoad.SELECTION_CHANGED)
    }

    inner class WheelPickerViewHolder(private val textView: TextView): ViewHolder(textView) {
        fun bind() {
            textView.text = getItem(adapterPosition)
            textView.textAlignment = TEXT_ALIGNMENT_CENTER
            textView.gravity = CENTER_VERTICAL

            setSelected(adapterPosition == selectedPosition)
        }

        fun setSelected(isSelected: Boolean) {
            textView.setTextAppearance(
                if (isSelected) R.style.Taboo_TextAppearance_WheelPicker_Selected
                else R.style.Taboo_TextAppearance_WheelPicker_Unselected
            )
        }
    }
}