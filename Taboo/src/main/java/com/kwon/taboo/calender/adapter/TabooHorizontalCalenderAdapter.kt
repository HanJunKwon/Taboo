package com.kwon.taboo.calender.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.NO_POSITION
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.kwon.taboo.calender.CalendarBlock
import com.kwon.taboo.databinding.TabooHorizontalCalenderItemBinding
import com.kwon.utils.calendar.CalendarUtils

/**
 * 하루의 밀리초
 */
private const val DAY_MILLIS = 24 * 60 * 60 * 1000

/**
 * 양쪽으로 스크롤하여 추가할 날짜 수
 */
private const val APPEND_SIZE = 10

class TabooHorizontalCalenderAdapter: RecyclerView.Adapter<ViewHolder>() {
    private var list = listOf<CalendarBlock>()
    private var clickListener: ((CalendarBlock) -> Unit)? = null
    private var changeListener: ((CalendarBlock) -> Unit)? = null
    private var selectedPosition = NO_POSITION

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
        for (i in 1 until APPEND_SIZE) {
            list += CalendarBlock(currentTimestamp + i * DAY_MILLIS)
        }

        // 앞에 날짜 추가
        for (i in 1..APPEND_SIZE) {
            list = listOf(CalendarBlock(currentTimestamp - i * DAY_MILLIS)) + list
        }

        notifyDataSetChanged()
    }

    fun appendCalendarBlock() {
        val currentTimestamp = list.last().timestamp
        val beforeSize = list.size

        // 뒤에 날짜 추가
        for (i in 1 until APPEND_SIZE) {
            list += CalendarBlock(currentTimestamp + i * DAY_MILLIS)
        }

        notifyItemRangeInserted(beforeSize, APPEND_SIZE)
    }

    fun prependCalendarBlock() {
        val currentTimestamp = list.first().timestamp

        // 앞에 날짜 추가
        for (i in 1..APPEND_SIZE) {
            list = listOf(CalendarBlock(currentTimestamp - i * DAY_MILLIS)) + list
        }

        selectedPosition += APPEND_SIZE

        notifyItemRangeInserted(0, APPEND_SIZE)
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

        // 업데이트
        if (previousPosition != NO_POSITION) {
            notifyItemChanged(previousPosition) // 이전 아이템 상태 업데이트
        }
        notifyItemChanged(selectedPosition) // 현재 아이템 상태 업데이트
    }

    fun setOnItemClickListener(listener: (CalendarBlock) -> Unit) {
        clickListener = listener
    }

    inner class TabooHorizontalCalenderViewHolder(private val binding: TabooHorizontalCalenderItemBinding): ViewHolder(binding.root) {
        fun bind(calendarBlock: CalendarBlock) {
            binding.tvDay.text = calendarBlock.getDay(CalendarUtils.KOREAN)
            binding.tvDate.text = calendarBlock.getDate()

            binding.root.isSelected = when {
                selectedPosition == NO_POSITION && isToday() -> {
                    selectedPosition = adapterPosition
                    true
                }
                else -> adapterPosition == selectedPosition
            }

            // 클릭 리스너 설정
            binding.root.setOnClickListener {
                setSelectedPosition(adapterPosition)

                clickListener?.invoke(calendarBlock)
            }
        }

        private fun isToday(): Boolean {
            return binding.tvDate.text == CalendarBlock(System.currentTimeMillis()).getDate()
        }
    }
}