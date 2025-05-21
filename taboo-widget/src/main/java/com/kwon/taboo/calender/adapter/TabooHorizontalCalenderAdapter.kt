package com.kwon.taboo.calender.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.NO_POSITION
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.kwon.taboo.calender.CalendarBlock
import com.kwon.taboo.calender.diffcallback.TabooCalendarDiffCallback
import com.kwon.taboo.databinding.TabooHorizontalCalenderItemBinding
import com.kwon.utils.calendar.CalendarUtils

class TabooHorizontalCalenderAdapter : ListAdapter<CalendarBlock, TabooHorizontalCalenderAdapter.TabooHorizontalCalenderViewHolder>(TabooCalendarDiffCallback()) {
    private var clickListener: ((CalendarBlock) -> Unit)? = null
    private var itemChangedListener: ((CalendarBlock) -> Unit)? = null
    private var monthChangedListener: ((timestamp: Long) -> Unit)? = null
    private var selectedCalendarBlock: CalendarBlock? = null

    private var locale: String = CalendarUtils.KOREAN

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TabooHorizontalCalenderViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = TabooHorizontalCalenderItemBinding.inflate(inflater, parent, false)
        return TabooHorizontalCalenderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TabooHorizontalCalenderViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isNotEmpty()) {
            holder.updateSelection(getItem(position).timestamp == selectedCalendarBlock?.timestamp)
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }


    override fun onBindViewHolder(holder: TabooHorizontalCalenderViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun initCalendarBlock() {
        val currentTimestamp = System.currentTimeMillis()
        setTimestamp(currentTimestamp)
    }

    fun setTimestamp(timestamp: Long) {
        // 이번달 날짜 추가
        val list = mutableListOf<CalendarBlock>()
        for (dateTimestamp in CalendarUtils.getMonthDates(timestamp)) {
            list += CalendarBlock(dateTimestamp)
        }

        submitList(list)

        monthChangedListener?.invoke(timestamp)
    }

    fun getPosition(timestamp: Long) : Int {
        val position = currentList.indexOfFirst { it.getFullDate() == CalendarBlock(timestamp).getFullDate() }
        return if (position == -1) NO_POSITION else position
    }

    fun getSelectedPosition(): Int {
        return currentList.indexOfFirst { it.getFullDate() == selectedCalendarBlock?.getFullDate() }
    }

    fun goSelectedDate() {
        selectedCalendarBlock?.timestamp?.let { setTimestamp(it) }
    }

    fun nextMonth() {
        setTimestamp(currentList.last().timestamp + 1000L * 60 * 60 * 24)
    }

    fun prevMonth() {
        setTimestamp(currentList.first().timestamp - 1000L * 60 * 60 * 24)
    }

    /**
     * 특정 날짜의 인덱스를 활성화할 때 사용
     */
    fun setSelectedPosition(position: Int) {
        // 범위 체크
        if (position > currentList.size - 1) {
            Log.e("TabooHorizontalCalenderAdapter", "position is out of range")
            return
        }

        // 이전 선택된 아이템 상태 업데이트
        selectedCalendarBlock?.let { prevCalendarBlock ->
            val previousPosition = currentList.indexOfFirst { it.getFullDate() == prevCalendarBlock.getFullDate() }
            if (previousPosition != NO_POSITION) {
                notifyItemChanged(previousPosition, "SELECTION_CHANGED")
            }
        }

        // 선택된 아이템 업데이트
        selectedCalendarBlock = currentList[position]
        notifyItemChanged(position, "SELECTION_CHANGED")

        // 날짜 변경 리스너 호출
        itemChangedListener?.invoke(currentList[position])
    }

    /**
     * 특정 날짜를 활성화할 때 사용
     */
    fun setSelectedCalendarBlock(calendarBlock: CalendarBlock) {
        val position = currentList.indexOfFirst { it.getFullDate() == calendarBlock.getFullDate() }
        setSelectedPosition(position)
    }

    fun getSelectedCalendarBlock() = selectedCalendarBlock

    fun setOnItemClickListener(listener: (CalendarBlock) -> Unit) {
        clickListener = listener
    }

    fun setOnItemChangeListener(listener: (CalendarBlock) -> Unit) {
        itemChangedListener = listener
    }

    fun setOnMonthChangedListener(listener: (timestamp: Long) -> Unit) {
        monthChangedListener = listener
    }

    fun setLocale(locale: String) {
        this.locale = locale
    }

    fun getLocale() = locale

    inner class TabooHorizontalCalenderViewHolder(private val binding: TabooHorizontalCalenderItemBinding): ViewHolder(binding.root) {
        fun bind(calendarBlock: CalendarBlock) {
            binding.tvDay.text = calendarBlock.getDay(locale)
            binding.tvDate.text = calendarBlock.getDate()

            binding.root.isSelected = when {
                selectedCalendarBlock == null && isToday(calendarBlock) -> {
                    selectedCalendarBlock = calendarBlock
                    true
                }
                else -> calendarBlock.getFullDate() == selectedCalendarBlock?.getFullDate()
            }

            // 클릭 리스너 설정
            binding.root.setOnClickListener {
                setSelectedPosition(adapterPosition)

                clickListener?.invoke(calendarBlock)
            }

            setVisibilityTodayDot(calendarBlock)
        }

        fun updateSelection(isSelected: Boolean) {
            binding.root.isSelected = isSelected
        }

        private fun isToday(calendarBlock: CalendarBlock): Boolean {
            return calendarBlock.getFullDate() == CalendarBlock(System.currentTimeMillis()).getFullDate()
        }

        private fun setVisibilityTodayDot(calendarBlock: CalendarBlock) {
            val todayDotVisibility = if (isToday(calendarBlock)) View.VISIBLE else View.GONE

            binding.viewTodayDot.visibility = todayDotVisibility
        }
    }
}