package com.kwon.taboosample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.kwon.taboo.button.TabooIconButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<TabooIconButton>(R.id.btn_taboo_text_button).apply {
            setOnClickListener {
                Log.d(">>>", "Taboo text button")
                this.isEnabled = !isEnabled
            }
        }
    }
}