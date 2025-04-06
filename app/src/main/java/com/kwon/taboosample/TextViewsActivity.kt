package com.kwon.taboosample

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kwon.taboo.textview.TabooPillTag

class TextViewsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_text_views)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<TabooPillTag>(R.id.tpt_change_color_1).setPillColor(
            primaryColor = ContextCompat.getColor(this, com.kwon.taboo.uicore.R.color.taboo_green_900),
            secondaryColor = ContextCompat.getColor(this, com.kwon.taboo.uicore.R.color.taboo_green_400)
        )

        findViewById<TabooPillTag>(R.id.tpt_change_color_2).setPillColor(
            primaryColor = ContextCompat.getColor(this, com.kwon.taboo.uicore.R.color.taboo_green_900),
            secondaryColor = ContextCompat.getColor(this, com.kwon.taboo.uicore.R.color.taboo_green_400)
        )
    }
}