package com.kwon.taboo.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.kwon.taboo.R

class TabooDropdownAdapter(
    context: Context,
    @LayoutRes private val layout: Int,
    private var items: Array<String> = arrayOf()
) : ArrayAdapter<String>(context, layout, items) {

    private var selectedPosition = NO_SELECTION

    /**
     * 선택된 아이템의 위치를 설정.
     */
    fun setSelectedPosition(position: Int) {
        selectedPosition = position
    }

    /**
     * 드랍다운된 아이템의 뷰를 설정.
     */
    override fun getView(position: Int, convertView: android.view.View?, parent: ViewGroup): android.view.View {
        LayoutInflater.from(context).inflate(layout, parent, false).let { layout ->
            return layout.apply {
                findViewById<TextView>(R.id.tv_dropdown_item).apply {
                    text = items[position]
                    isSelected = (position == selectedPosition)
                }
            }
        }
    }

    /**
     * 드랍다운된 아이템의 뷰를 설정.
     */
    override fun getDropDownView(position: Int, convertView: android.view.View?, parent: ViewGroup): android.view.View {
        LayoutInflater.from(context).inflate(layout, parent, false).let { layout ->
            return layout.apply {
                findViewById<TextView>(R.id.tv_dropdown_item).apply {
                    text = items[position]
                    isSelected = (position == selectedPosition)
                }
            }
        }
    }
}