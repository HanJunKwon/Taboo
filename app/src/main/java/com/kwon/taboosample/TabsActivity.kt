package com.kwon.taboosample

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kwon.taboo.button.TabooButton
import com.kwon.taboo.tabs.TabooTabBlock
import com.kwon.taboo.tabs.TabooTabLayout
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

        findViewById<TabooButton>(R.id.btn_add_tab_at_last).setOnClickListener {
            findViewById<TabooTabLayout>(R.id.ttl_tabs).addTab(TabooTabBlock("Tab 4", Random.nextInt(), tabIcon = R.drawable.round_bluetooth_24))
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
    }
}