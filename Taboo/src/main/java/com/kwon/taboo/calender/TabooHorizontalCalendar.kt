package com.kwon.taboo.calender

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.kwon.taboo.R
import com.kwon.taboo.calender.adapter.TabooHorizontalCalenderAdapter
import com.kwon.taboo.calender.decoration.CalendarHorizontalSpaceDecoration
import com.kwon.taboo.databinding.TabooHorizontalCalenderBinding

class TabooHorizontalCalendar(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {
    private val binding = TabooHorizontalCalenderBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        val typed = context.obtainStyledAttributes(attrs, R.styleable.TabooHorizontalCalender)

        typed.recycle()

        setCalendar()
    }

    /**
     * Set calendar
     */
    private fun setCalendar() {
        binding.rvHorizontalCalender.adapter = TabooHorizontalCalenderAdapter()
        binding.rvHorizontalCalender.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvHorizontalCalender.addItemDecoration(CalendarHorizontalSpaceDecoration(20))
        (binding.rvHorizontalCalender.adapter as TabooHorizontalCalenderAdapter).initCalendarBlock()
    }

    fun setOnItemClickListener(listener: (CalendarBlock) -> Unit) {
        (binding.rvHorizontalCalender.adapter as TabooHorizontalCalenderAdapter).setOnItemClickListener(listener)
    }

    fun setSelectedPosition(position: Int) {
        (binding.rvHorizontalCalender.adapter as TabooHorizontalCalenderAdapter).setSelectedPosition(position)
    }

    fun setSelectedCalendarBlock(calendarBlock: CalendarBlock) {
        (binding.rvHorizontalCalender.adapter as TabooHorizontalCalenderAdapter).setSelectedCalendarBlock(calendarBlock)
    }
}