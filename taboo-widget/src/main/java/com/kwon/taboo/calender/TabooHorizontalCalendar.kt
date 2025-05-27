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
    private val rootView = LayoutInflater.from(context).inflate(R.layout.taboo_horizontal_calender, this, true)

    init {
        val typed = context.obtainStyledAttributes(attrs, R.styleable.TabooHorizontalCalender)

        typed.recycle()

        setCalendar()
    }

    /**
     * Set calendar
     */
    private fun setCalendar() {
        rootView.findViewById<RecyclerView>(R.id.rv_horizontal_calender).apply {
            adapter = TabooHorizontalCalenderAdapter().apply {
                initCalendarBlock()
            }
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            itemAnimator = null
            addItemDecoration(CalendarHorizontalSpaceDecoration(20))
        }
    }

    fun setLocale(locale: String) {
        getAdapter().setLocale(locale)
    }

    fun getLocale(): String {
        return getAdapter().getLocale()
    }

    fun setTimestamp(timestamp: Long) {
        getAdapter().setTimestamp(timestamp)
    }

    fun goSelectedDate() {
        val horizontalCalendar = rootView.findViewById<RecyclerView>(R.id.rv_horizontal_calender)
        val adapter = horizontalCalendar.adapter as TabooHorizontalCalenderAdapter
        val position = getAdapter().getSelectedPosition()

        if (position == -1) {
            adapter.goSelectedDate()

            horizontalCalendar.viewTreeObserver.addOnPreDrawListener(
                object : ViewTreeObserver.OnPreDrawListener {
                    override fun onPreDraw(): Boolean {
                        rootView.findViewById<RecyclerView>(R.id.rv_horizontal_calender).viewTreeObserver.removeOnPreDrawListener(this)

                        // Position 을 다시 계산하기 위해 재귀 호출
                        goSelectedDate()
                        return true
                    }
                }
            )
        } else {
            horizontalCalendar.scrollToPositionWithCenter(context, position)
        }
    }

    fun goToday() {
        val horizontalCalendar = rootView.findViewById<RecyclerView>(R.id.rv_horizontal_calender)
        val adapter = horizontalCalendar.adapter as TabooHorizontalCalenderAdapter
        val position = adapter.getPosition(System.currentTimeMillis())

        if (position == -1) {
            setTimestamp(System.currentTimeMillis())

            rootView.findViewById<RecyclerView>(R.id.rv_horizontal_calender).viewTreeObserver.addOnPreDrawListener(
                object : ViewTreeObserver.OnPreDrawListener {
                    override fun onPreDraw(): Boolean {
                        rootView.findViewById<RecyclerView>(R.id.rv_horizontal_calender).viewTreeObserver.removeOnPreDrawListener(this)
                        goToday()
                        return true
                    }
                }
            )
        } else {
            rootView.findViewById<RecyclerView>(R.id.rv_horizontal_calender).scrollToPositionWithCenter(context, position)

            setSelectedPosition(position)
        }
    }

    fun nextMonth() {
        getAdapter().nextMonth()
    }

    fun prevMonth() {
        getAdapter().prevMonth()
    }

    fun setOnItemClickListener(listener: (CalendarBlock) -> Unit) {
        getAdapter().setOnItemClickListener(listener)
    }

    fun setOnItemChangedListener(listener: (CalendarBlock) -> Unit) {
        getAdapter().setOnItemChangeListener(listener)
    }

    fun setOnMonthChangedListener(listener: (Long) -> Unit) {
        getAdapter().setOnMonthChangedListener(listener)
    }

    fun setSelectedPosition(position: Int) {
        getAdapter().setSelectedPosition(position)
    }

    fun setSelectedCalendarBlock(calendarBlock: CalendarBlock) {
        getAdapter().setSelectedCalendarBlock(calendarBlock)
    }

    fun getSelectedCalendarBlock(): CalendarBlock? {
        return getAdapter().getSelectedCalendarBlock()
    }

    fun getAdapter(): TabooHorizontalCalenderAdapter {
        return rootView.findViewById<RecyclerView>(R.id.rv_horizontal_calender).adapter as TabooHorizontalCalenderAdapter
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