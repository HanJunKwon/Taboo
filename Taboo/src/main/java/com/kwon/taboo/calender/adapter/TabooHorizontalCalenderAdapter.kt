package com.kwon.taboo.calender.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.kwon.taboo.calender.CalendarBlock
import com.kwon.taboo.databinding.TabooHorizontalCalenderItemBinding
import com.kwon.utils.calendar.CalendarUtils

/**
 * 하루의 밀리초
 */
private const val DAY_MILLIS = 24 * 60 * 60 * 1000

/**
 * 가로로 스크롤되는 캘린더 어댑터
 */
class TabooHorizontalCalenderAdapter: RecyclerView.Adapter<ViewHolder>() {
    private var list = listOf<CalendarBlock>()
    private var selectedCalendarBlock: View? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = TabooHorizontalCalenderItemBinding.inflate(inflater, parent, false)
        return TabooHorizontalCalenderViewHolder(binding)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        (holder as TabooHorizontalCalenderViewHolder).bind(list[position])
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

        // 스크롤이 끝에 도달하면 뒤에 날짜 추가
        recyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (!recyclerView.canScrollHorizontally(1)) {
                    appendCalendarBlock()
                } else if (!recyclerView.canScrollHorizontally(-1)) {
                    prependCalendarBlock()
                }
            }
        })
    }

    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)
        (holder as TabooHorizontalCalenderViewHolder)
    }

    fun initCalendarBlock() {
        val currentTimestamp = System.currentTimeMillis()

        // 오늘 날짜 추가
        list = listOf(CalendarBlock(currentTimestamp))

        // 뒤에 날짜 추가
        for (i in 1 until 10) {
            list += CalendarBlock(currentTimestamp + i * DAY_MILLIS)
        }

        // 앞에 날짜 추가
        for (i in 1..10) {
            list = listOf(CalendarBlock(currentTimestamp - i * DAY_MILLIS)) + list
        }

        notifyDataSetChanged()
    }

    fun appendCalendarBlock() {
        val currentTimestamp = list.last().timestamp
        val beforeSize = list.size

        // 뒤에 날짜 추가
        for (i in 1 until 10) {
            list += CalendarBlock(currentTimestamp + i * DAY_MILLIS)
        }

        notifyItemRangeInserted(beforeSize, 10)
    }

    fun prependCalendarBlock() {
        val currentTimestamp = list.first().timestamp

        // 앞에 날짜 추가
        for (i in 1..10) {
            list = listOf(CalendarBlock(currentTimestamp - i * DAY_MILLIS)) + list
        }

        notifyItemRangeInserted(0, 10)
    }

    inner class TabooHorizontalCalenderViewHolder(private val binding: TabooHorizontalCalenderItemBinding): ViewHolder(binding.root) {
        fun bind(calendarBlock: CalendarBlock) {
            binding.tvDay.text = calendarBlock.getDay(CalendarUtils.KOREAN)
            binding.tvDate.text = calendarBlock.getDate()

            binding.root.setOnClickListener {
                selectedCalendarBlock?.isSelected = false
                setSelectedCalendarBlock(binding.root)
            }

            if (isToday()) {
                setSelectedCalendarBlock(binding.root)
            }
        }

        private fun isToday(): Boolean {
            return binding.tvDate.text == CalendarBlock(System.currentTimeMillis()).getDate()
        }

        fun setSelectedCalendarBlock(view: View) {
            selectedCalendarBlock = view
            view.isSelected = true
        }
    }
}