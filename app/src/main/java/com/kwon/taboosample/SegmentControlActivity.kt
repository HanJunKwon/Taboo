package com.kwon.taboosample

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kwon.taboo.button.TabooButton
import com.kwon.taboo.segment.TabooSegmentControl
import com.kwon.taboo.segment.TabooSegmentTab
import com.kwon.taboo.uicore.util.ResourceUtils

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

        findViewById<TabooSegmentControl>(R.id.tsc_segment_control_outline_type).apply {
            setItems(listOf("세그먼트 1", "세그먼트 2", "세그먼트 3", "세그먼트 4", "세그먼트 5"))
            setOnItemClickListener {
                Log.d(">>>", "Clicked $it")
            }

            setOnSelectedItemChangedListener {
                Log.d(">>>", "Selected $it")
            }

            setSelectedItem(0)
        }

        findViewById<TabooSegmentControl>(R.id.tsc_segment_control_solid_type).apply {
            setItems(listOf("세그먼트 1", "세그먼트 2", "세그먼트 3", "세그먼트 4", "세그먼트 5"))
            setOnItemClickListener {
                Log.d(">>>", "Clicked $it")
            }

            setOnSelectedItemChangedListener {
                Log.d(">>>", "Selected $it")
            }

            setSelectedItem(0)
        }

        findViewById<TabooSegmentTab>(R.id.tst_segment_tab).setItems(listOf("탭1", "탭2", "탭3", "탭4"))
        findViewById<TabooSegmentTab>(R.id.tst_segment_tab).setOnTabSelectedListener {
            Log.d(">>>", "Selected tab $it")
        }

        findViewById<TabooSegmentTab>(R.id.tst_segment_tab_2).setItems(listOf("탭1", "탭2", "탭3", "탭4"))
        findViewById<TabooSegmentTab>(R.id.tst_segment_tab_2).setOnTabSelectedListener {
            Log.d(">>>", "Selected tab $it")
            if (it == 0) {
                findViewById<TabooSegmentTab>(R.id.tst_segment_tab_2).setTabPadding(ResourceUtils.dpToPx(this@SegmentControlActivity, 30f))
            }
        }
        findViewById<TabooSegmentTab>(R.id.tst_segment_tab_2).setSelectedIndex(2)
        findViewById<TabooButton>(R.id.btn_move_selected_to_first).setOnClickListener {
            findViewById<TabooSegmentTab>(R.id.tst_segment_tab_2).setSelectedIndex(1)
        }
    }
}