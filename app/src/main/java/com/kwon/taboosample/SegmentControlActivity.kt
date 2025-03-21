package com.kwon.taboosample

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kwon.taboo.adapter.TabooSegmentControlAdapter
import com.kwon.taboo.segment.TabooSegmentControl

class SegmentControlActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_segment_control)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<TabooSegmentControl>(R.id.tsc_segment_control).apply {
            setItems(listOf("세그먼트 1", "세그먼트 2", "세그먼트 3", "세그먼트 4", "세그먼트 5"))
            setOnItemClickListener {
                Log.d(">>>", "Clicked $it")
            }
        }
    }
}