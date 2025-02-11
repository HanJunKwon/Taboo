package com.kwon.taboosample

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kwon.taboo.button.TabooButton
import com.kwon.taboo.calender.TabooHorizontalCalendar

class CalendarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_calendar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<TabooHorizontalCalendar>(R.id.calendar).setOnItemChangedListener {
            val fullDateTextView = findViewById<TextView>(R.id.tv_full_date)
            fullDateTextView.text = "선택한 날짜(연월일): " + it.getFullDate()

            val dateTextView = findViewById<TextView>(R.id.tv_date)
            dateTextView.text = "선택한 날짜(일): " + it.getDate()

            val dayTextView = findViewById<TextView>(R.id.tv_day)
            dayTextView.text = "선택한 요일: " + it.getDay("ko")
        }

        findViewById<TabooButton>(R.id.btn_prev).setOnClickListener {
            findViewById<TabooHorizontalCalendar>(R.id.calendar).prevMonth()
        }

        findViewById<TabooButton>(R.id.btn_next).setOnClickListener {
            findViewById<TabooHorizontalCalendar>(R.id.calendar).nextMonth()
        }

        findViewById<TabooButton>(R.id.btn_today).setOnClickListener {
            findViewById<TabooHorizontalCalendar>(R.id.calendar).goToday()
        }
    }
}