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
    }
}