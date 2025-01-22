package com.kwon.taboo.calender.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.NO_POSITION
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.kwon.taboo.calender.CalendarBlock
import com.kwon.taboo.databinding.TabooHorizontalCalenderItemBinding
import com.kwon.utils.calendar.CalendarUtils

class TabooHorizontalCalenderAdapter: RecyclerView.Adapter<ViewHolder>() {
    private var list = listOf<CalendarBlock>()
    private var clickListener: ((CalendarBlock) -> Unit)? = null
    private var changeListener: ((CalendarBlock) -> Unit)? = null
    private var calendarUpdatedListener : (() -> Unit)? = null
    private var selectedPosition = NO_POSITION
    private var selectedCalendarBlock: CalendarBlock? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = TabooHorizontalCalenderItemBinding.inflate(inflater, parent, false)
        return TabooHorizontalCalenderViewHolder(binding)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        (holder as TabooHorizontalCalenderViewHolder).bind(list[position])
    }

    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)
        (holder as TabooHorizontalCalenderViewHolder)
    }

    fun initCalendarBlock() {
        val currentTimestamp = System.currentTimeMillis()
        setTimestamp(currentTimestamp)
    }

    fun setTimestamp(timestamp: Long, listener: ((CalendarBlock) -> Unit)? = null) {
        setTimestamp(timestamp)
    }

    fun setTimestamp(timestamp: Long) {
        list = listOf()
        val monthDates = CalendarUtils.getMonthDates(timestamp)

        // 이번달 날짜 추가
        for (dateTimestamp in monthDates) {
            list += CalendarBlock(dateTimestamp)
        }

        notifyDataSetChanged()
    }

    fun getPosition(timestamp: Long) : Int {
        val position = list.indexOfFirst { it.getFullDate() == CalendarBlock(timestamp).getFullDate() }
        return if (position == -1) NO_POSITION else position
    }

    fun nextMonth() {
        selectedPosition = NO_POSITION
        selectedCalendarBlock = null
        setTimestamp(list.last().timestamp + 1000L * 60 * 60 * 24)
    }

    fun prevMonth() {
        selectedPosition = NO_POSITION
        selectedCalendarBlock = null
        setTimestamp(list.first().timestamp - 1000L * 60 * 60 * 24)
    }

    /**
     * 특정 날짜의 인덱스를 활성화할 때 사용
     */
    fun setSelectedPosition(position: Int) {
        if (position == selectedPosition) {
            return
        }

        if (position > list.size - 1) {
            Log.e("TabooHorizontalCalenderAdapter", "position is out of range")
            return
        }

        val previousPosition = selectedPosition
        selectedPosition = position
        selectedCalendarBlock = list[position]

        // 업데이트
        if (previousPosition != NO_POSITION) {
            notifyItemChanged(previousPosition) // 이전 아이템 상태 업데이트
        }
        notifyItemChanged(selectedPosition) // 현재 아이템 상태 업데이트

        changeListener?.invoke(list[selectedPosition])
    }

    /**
     * 특정 날짜를 활성화할 때 사용
     */
    fun setSelectedCalendarBlock(calendarBlock: CalendarBlock) {
        val position = list.indexOfFirst { it.getFullDate() == calendarBlock.getFullDate() }
        setSelectedPosition(position)
    }

    fun setOnItemClickListener(listener: (CalendarBlock) -> Unit) {
        clickListener = listener
    }

    inner class TabooHorizontalCalenderViewHolder(private val binding: TabooHorizontalCalenderItemBinding): ViewHolder(binding.root) {
        fun bind(calendarBlock: CalendarBlock) {
            binding.tvDay.text = calendarBlock.getDay(CalendarUtils.KOREAN)
            binding.tvDate.text = calendarBlock.getDate()

            binding.root.isSelected = when {
                selectedPosition == NO_POSITION && selectedCalendarBlock == null && isToday(calendarBlock) -> {
                    selectedPosition = adapterPosition
                    selectedCalendarBlock = calendarBlock
                    true
                }
                else -> adapterPosition == selectedPosition && calendarBlock.getFullDate() == selectedCalendarBlock?.getFullDate()
            }

            // 클릭 리스너 설정
            binding.root.setOnClickListener {
                setSelectedPosition(adapterPosition)

                clickListener?.invoke(calendarBlock)
            }

            setVisibilityTodayDot(calendarBlock)
        }

        private fun isToday(calendarBlock: CalendarBlock): Boolean {
            return calendarBlock.getFullDate() == CalendarBlock(System.currentTimeMillis()).getFullDate()
        }

        private fun setVisibilityTodayDot(calendarBlock: CalendarBlock) {
            val todayDotVisibility = if (isToday(calendarBlock) && !binding.root.isSelected) View.VISIBLE else View.GONE

            binding.viewTodayDot.visibility = todayDotVisibility
        }
    }
}