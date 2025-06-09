package com.kwon.taboosample

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kwon.taboo.counter.TabooCounter

class CountersActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_counters)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<TabooCounter>(R.id.counter_1).apply {
            setOnCountClickListener(object: TabooCounter.OnCountClickListener {
                override fun onMinusClicked() {
                    Log.d("Counter", "Minus clicked, current count")
                }

                override fun onPlusClicked() {
                    Log.d("Counter", "Minus clicked, current count")
                }
            })

            setOnCountChangeListener(object: TabooCounter.OnCountChangeListener {
                override fun onCountChanged(count: Int) {
                    Log.d("Counter", "Count changed to $count")
                }
            })
        }
    }
}