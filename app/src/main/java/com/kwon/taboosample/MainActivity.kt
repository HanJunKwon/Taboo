package com.kwon.taboosample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.kwon.taboo.button.TabooIconButton
import com.kwon.taboo.button.TabooPreviewButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<TabooPreviewButton>(R.id.btn_preview).setOnClickListener {
            Log.d(">>>", "buttonClick")
        }
    }
}