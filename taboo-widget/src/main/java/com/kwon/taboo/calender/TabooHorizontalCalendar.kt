package com.kwon.taboo.calender

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewTreeObserver
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.doOnPreDraw
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
        binding.rvHorizontalCalender.apply {
            adapter = TabooHorizontalCalenderAdapter().apply {
                initCalendarBlock()
            }
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(CalendarHorizontalSpaceDecoration(20))
        }
    }

    fun setLocale(locale: String) {
        (binding.rvHorizontalCalender.adapter as TabooHorizontalCalenderAdapter).setLocale(locale)
    }

    fun getLocale(): String {
        return (binding.rvHorizontalCalender.adapter as TabooHorizontalCalenderAdapter).getLocale()
    }

    fun setTimestamp(timestamp: Long) {
        (binding.rvHorizontalCalender.adapter as TabooHorizontalCalenderAdapter).setTimestamp(timestamp)
    }

    fun goSelectedDate() {
        val horizontalCalendar = binding.rvHorizontalCalender
        val adapter = horizontalCalendar.adapter as TabooHorizontalCalenderAdapter
        val position = adapter.getSelectedPosition()

        if (position == -1) {
            adapter.goSelectedDate()

            horizontalCalendar.viewTreeObserver.addOnPreDrawListener(
                object : ViewTreeObserver.OnPreDrawListener {
                    override fun onPreDraw(): Boolean {
                        binding.rvHorizontalCalender.viewTreeObserver.removeOnPreDrawListener(this)

                        // Position 을 다시 계산하기 위해 재귀 호출
                        goSelectedDate()
                        return true
                    }
                }
            )
        } else {
            horizontalCalendar.scrollToPositionWithCenter(binding.root.context, position)
        }
    }

    fun goToday() {
        val position = (binding.rvHorizontalCalender.adapter as TabooHorizontalCalenderAdapter).getPosition(System.currentTimeMillis())
        if (position == -1) {
            setTimestamp(System.currentTimeMillis())

            binding.rvHorizontalCalender.viewTreeObserver.addOnPreDrawListener(
                object : ViewTreeObserver.OnPreDrawListener {
                    override fun onPreDraw(): Boolean {
                        binding.rvHorizontalCalender.viewTreeObserver.removeOnPreDrawListener(this)
                        goToday()
                        return true
                    }
                }
            )
        } else {
            binding.rvHorizontalCalender.scrollToPositionWithCenter(binding.root.context, position)

            setSelectedPosition(position)
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

    fun setOnItemChangedListener(listener: (CalendarBlock) -> Unit) {
        (binding.rvHorizontalCalender.adapter as TabooHorizontalCalenderAdapter).setOnItemChangeListener(listener)
    }

    fun setOnMonthChangedListener(listener: (Long) -> Unit) {
        (binding.rvHorizontalCalender.adapter as TabooHorizontalCalenderAdapter).setOnMonthChangedListener(listener)
    }

    fun setSelectedPosition(position: Int) {
        (binding.rvHorizontalCalender.adapter as TabooHorizontalCalenderAdapter).setSelectedPosition(position)
    }

    fun setSelectedCalendarBlock(calendarBlock: CalendarBlock) {
        (binding.rvHorizontalCalender.adapter as TabooHorizontalCalenderAdapter).setSelectedCalendarBlock(calendarBlock)
    }

    fun getSelectedCalendarBlock(): CalendarBlock? {
        return (binding.rvHorizontalCalender.adapter as TabooHorizontalCalenderAdapter).getSelectedCalendarBlock()
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