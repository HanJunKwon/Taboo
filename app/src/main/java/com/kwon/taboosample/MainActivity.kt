package com.kwon.taboosample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btn_button_example).setOnClickListener {
            startActivity(Intent(this, ButtonsActivity::class.java))
        }

        findViewById<Button>(R.id.btn_edit_text_example).setOnClickListener {
            startActivity(Intent(this, EditTextsActivity::class.java))
        }

        findViewById<Button>(R.id.btn_dialog_example).setOnClickListener {
            startActivity(Intent(this, DialogsActivity::class.java))
        }

        findViewById<Button>(R.id.btn_calendar_example).setOnClickListener {
            startActivity(Intent(this, CalendarActivity::class.java))
        }

        findViewById<Button>(R.id.btn_loading_example).setOnClickListener {
            startActivity(Intent(this, LoadingsActivity::class.java))
        }

        findViewById<Button>(R.id.btn_metric_card_example).setOnClickListener {
            startActivity(Intent(this, MetricsCardActivity::class.java))
        }

        findViewById<Button>(R.id.btn_tab_example).setOnClickListener {
            startActivity(Intent(this, TabsActivity::class.java))
        }

        findViewById<Button>(R.id.btn_numbering_ball_example).setOnClickListener {
            startActivity(Intent(this, NumberingBallsActivity::class.java))
        }

        findViewById<Button>(R.id.btn_preview_button_example).setOnClickListener {
            startActivity(Intent(this, PreviewButtonsActivity::class.java))
        }

        findViewById<Button>(R.id.btn_wheel_picker_example).setOnClickListener {
            startActivity(Intent(this, WheelPickerActivity::class.java))
        }

        findViewById<Button>(R.id.btn_text_view_example).setOnClickListener {
            startActivity(Intent(this, TextViewsActivity::class.java))
        }

        findViewById<Button>(R.id.btn_radio_button_example).setOnClickListener {
            startActivity(Intent(this, RadiosActivity::class.java))
        }

        findViewById<Button>(R.id.btn_segment_control_example).setOnClickListener {
            startActivity(Intent(this, SegmentControlActivity::class.java))
        }

        findViewById<Button>(R.id.btn_timer_example).setOnClickListener {
            startActivity(Intent(this, TimersActivity::class.java))
        }

        findViewById<Button>(R.id.btn_counter_example).setOnClickListener {
            startActivity(Intent(this, CountersActivity::class.java))
        }

        findViewById<Button>(R.id.btn_bottom_navigation_example).setOnClickListener {
            startActivity(Intent(this, BottomNavigationActivity::class.java))
        }

        findViewById<Button>(R.id.btn_fade_scroll_view_example).setOnClickListener {
            startActivity(Intent(this, FadeScrollViewActivity::class.java))
        }

        findViewById<Button>(R.id.btn_bottom_up_view_example).setOnClickListener {
            startActivity(Intent(this, BottomUpLayoutActivity::class.java))
        }
    }
}