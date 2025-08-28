package com.kwon.taboosample

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kwon.taboo.adapter.TabooTabAdapter
import com.kwon.taboo.button.TabooButton
import com.kwon.taboo.tabs.TabooTabBlock
import com.kwon.taboo.tabs.TabooTabLayout
import com.hansae.taboo.core.util.ResourceUtils
import kotlin.random.Random

class TabsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tabs)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<TabooTabLayout>(R.id.ttl_tabs).setTabSelectedListener(listener = object: TabooTabAdapter.TabSelectedListener {
            override fun onTabSelected(index: Int) {
                Log.d(">>>", "onTabSelected: $index")
            }
        })

        findViewById<TabooButton>(R.id.btn_add_tab_at_last).setOnClickListener {
            findViewById<TabooTabLayout>(R.id.ttl_tabs).addTab(TabooTabBlock("Tab 4", Random.nextInt(), tabIcon = R.drawable.round_bluetooth_24))
            findViewById<TabooTabLayout>(R.id.ttl_tabs).addTab(TabooTabBlock("Tab 4", Random.nextInt(), tabIcon = R.drawable.round_bluetooth_24))
//            findViewById<TabooTabLayout>(R.id.ttl_tabs).addTab(
//                listOf<TabooTabBlock>(
//                    TabooTabBlock("Tab 1", Random.nextInt(), tabIcon = R.drawable.round_bluetooth_24),
//                    TabooTabBlock("Tab 2", Random.nextInt(), tabIcon = R.drawable.round_bluetooth_24),
//                    TabooTabBlock("Tab 3", Random.nextInt(), tabIcon = R.drawable.round_bluetooth_24),
//                    TabooTabBlock("Tab 4", Random.nextInt(), tabIcon = R.drawable.round_bluetooth_24)
//                )
//            )
        }

        findViewById<TabooButton>(R.id.btn_add_tab_at_first).setOnClickListener {
            findViewById<TabooTabLayout>(R.id.ttl_tabs).addTab(TabooTabBlock("Tab 0", Random.nextInt()), 0)
        }

        findViewById<TabooButton>(R.id.btn_remove_tab_at_first).setOnClickListener {
            findViewById<TabooTabLayout>(R.id.ttl_tabs).removeTab(0)
        }

        findViewById<TabooButton>(R.id.btn_change_numbering_visibility).setOnClickListener {
            findViewById<TabooTabLayout>(R.id.ttl_tabs).isVisibilityNumbering(!findViewById<TabooTabLayout>(R.id.ttl_tabs).isVisibilityNumbering())
        }

        findViewById<TabooButton>(R.id.btn_change_icon_visibility).setOnClickListener {
            findViewById<TabooTabLayout>(R.id.ttl_tabs).isVisibilityIcon(!findViewById<TabooTabLayout>(R.id.ttl_tabs).isVisibilityIcon())
        }

        findViewById<TabooButton>(R.id.btn_get_tab_selected_index).setOnClickListener {
            Log.d(">>>", "selected Tab Index: ${findViewById<TabooTabLayout>(R.id.ttl_tabs).getSelectedTabPosition()}")
        }

        findViewById<TabooButton>(R.id.btn_set_tab_padding).setOnClickListener {
            findViewById<TabooTabLayout>(R.id.ttl_tabs).setTabPadding(ResourceUtils.dpToPx(this, 10f))
        }
    }
}