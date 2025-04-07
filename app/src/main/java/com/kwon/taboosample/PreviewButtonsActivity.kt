package com.kwon.taboosample

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kwon.taboo.button.TabooMenuButton

class PreviewButtonsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_preview_buttons)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<TabooMenuButton>(R.id.tmb_language).setOnClickListener {
            Log.d(">>>", "Language button clicked")
        }
        findViewById<TabooMenuButton>(R.id.tmb_theme).setOnCheckedChangeListener { _, isToggle ->
            Log.d(">>>", "Theme button clicked: $isToggle")
        }
        findViewById<TabooMenuButton>(R.id.tmb_theme).setOnClickListener {
            Log.d(">>>", "Theme button clicked")
        }

        findViewById<TabooMenuButton>(R.id.tmb_theme).isToggleChecked(true)
    }
}