package com.kwon.taboosample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import com.kwon.taboo.button.TabooIconButton
import com.kwon.taboo.button.TabooPreviewButton
import com.kwon.taboo.edittext.TabooEditText

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<TabooPreviewButton>(R.id.btn_preview).apply {
            setOnClickListener {
                Log.d(">>>", "buttonClick")
            }
//            setIconResource(ContextCompat.getDrawable(this@MainActivity, R.drawable.ic_hotel_48dp))
            setIconResourceId(R.drawable.ic_hotel_48dp)
        }

        findViewById<TabooEditText>(R.id.edt_text).getText()
    }
}