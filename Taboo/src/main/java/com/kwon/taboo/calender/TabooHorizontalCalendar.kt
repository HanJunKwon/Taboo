package com.kwon.taboo.calender

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kwon.taboo.R
import com.kwon.taboo.calender.adapter.TabooHorizontalCalenderAdapter
import com.kwon.taboo.calender.decoration.CalendarHorizontalSpaceDecoration
import com.kwon.taboo.calender.scroller.CenterSmoothScroller
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

    fun setTimestamp(timestamp: Long) {
        (binding.rvHorizontalCalender.adapter as TabooHorizontalCalenderAdapter).setTimestamp(timestamp)
    }

    fun goToday() {
        val position = (binding.rvHorizontalCalender.adapter as TabooHorizontalCalenderAdapter).getPosition(System.currentTimeMillis())
        if (position != -1) {
            binding.rvHorizontalCalender.scrollToPositionWithCenter(binding.root.context, position)
        }
    }

    fun nextMonth() {
        (binding.rvHorizontalCalender.adapter as TabooHorizontalCalenderAdapter).nextMonth()
    }

    fun prevMonth() {
        (binding.rvHorizontalCalender.adapter as TabooHorizontalCalenderAdapter).prevMonth()
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

    private fun RecyclerView.scrollToPositionWithCenter(context: Context, position: Int) {
        val layoutManager = this.layoutManager as? LinearLayoutManager
        if (layoutManager != null) {
            val smoothScroller = CenterSmoothScroller(context)
            smoothScroller.targetPosition = position
            layoutManager.startSmoothScroll(smoothScroller)
        }
    }
}