package com.kwon.taboosample

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kwon.taboo.toast.TabooToast
import com.kwon.taboo.button.TabooBadgeButton
import com.kwon.taboo.button.TabooButton
import com.kwon.taboo.button.TabooGhostButton

class ButtonsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_buttons)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<TabooButton>(R.id.btn_solid).setOnClickListener {
            TabooToast(this)
                .makeText(com.kwon.taboo.R.drawable.ic_success, "Solid Button Clicked", Toast.LENGTH_SHORT)
                .show()
        }

        findViewById<TabooButton>(R.id.btn_fill).setOnClickListener {
            TabooToast(this)
                .makeText(com.kwon.taboo.R.drawable.ic_success, "Fill Button Clicked", Toast.LENGTH_SHORT)
                .show()
        }

        findViewById<TabooBadgeButton>(R.id.btn_badge).setBadge(10)

        findViewById<TabooGhostButton>(R.id.btn_add_machine).apply {
            setOnClickListener { Log.d(">>>", "Add Machine Button Clicked") }
            setOnLongClickListener {
                Log.d(">>>", "Add Machine Button Long Clicked")
                true
            }
        }
    }
}