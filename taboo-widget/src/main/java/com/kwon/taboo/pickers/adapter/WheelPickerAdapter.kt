package com.kwon.taboo.pickers.adapter

import android.content.Context
import android.util.Log
import android.view.Gravity.CENTER_VERTICAL
import android.view.View.TEXT_ALIGNMENT_TEXT_START
import android.view.View.TEXT_ALIGNMENT_VIEW_END
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
    private var itemHeightPixel: Int = ResourceUtils.dpToPx(context, 32f).toInt()
): ListAdapter<String, WheelPickerAdapter.WheelPickerViewHolder>(TabooWheelPickerDiffCallback()) {
    private var textGravity: Int = 0

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
                    PayLoad.TEXT_GRAVITY_CHANGED -> {
                        holder.updateTextGravity()
                    }
                    PayLoad.ITEM_HEIGHT_CHANGED -> {
                        holder.updateItemHeight()
                    }
                }
            }
        }
    }

    private fun createPickerTextView(): TextView {
        return TextView(context).apply {
            layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, itemHeightPixel)
            textAlignment = TEXT_ALIGNMENT_CENTER
            gravity = TEXT_ALIGNMENT_CENTER
            setTextColor(ContextCompat.getColor(context, com.kwon.taboo.uicore.R.color.taboo_black))
        }
    }

    fun selectedPosition (position: Int) {
        if (selectedPosition == position) return
        selectedPosition = position
        notifyItemRangeChanged(0, itemCount, PayLoad.SELECTION_CHANGED)
    }

    fun setTextGravity(gravity: Int) {
        textGravity = gravity
        notifyItemRangeChanged(0, itemCount, PayLoad.TEXT_GRAVITY_CHANGED)
    }

    fun setItemHeight(heightPixel: Int) {
        Log.d(">>>", "setItemHeight: $heightPixel")
        itemHeightPixel = heightPixel
        notifyItemRangeChanged(0, itemCount, PayLoad.ITEM_HEIGHT_CHANGED)
    }

    inner class WheelPickerViewHolder(private val textView: TextView): ViewHolder(textView) {
        fun bind() {
            textView.text = getItem(adapterPosition)

            updateTextGravity()
            setSelected(adapterPosition == selectedPosition)
        }

        fun setSelected(isSelected: Boolean) {
            textView.setTextAppearance(
                if (isSelected) R.style.Taboo_TextAppearance_WheelPicker_Selected
                else R.style.Taboo_TextAppearance_WheelPicker_Unselected
            )
        }

        fun updateTextGravity() {
            textView.textAlignment =
                when (textGravity) {
                    0 -> TEXT_ALIGNMENT_TEXT_START
                    1 -> TEXT_ALIGNMENT_CENTER
                    2 -> TEXT_ALIGNMENT_VIEW_END
                    else -> TEXT_ALIGNMENT_CENTER
                }
            textView.gravity = CENTER_VERTICAL
        }

        fun updateItemHeight() {
            textView.layoutParams = textView.layoutParams.apply {
                height = ResourceUtils.pxToDp(context, itemHeightPixel).toInt()
            }
        }
    }
}