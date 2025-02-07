package com.kwon.taboosample

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kwon.taboo.edittext.TabooEditText

class EditTextsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_texts)
        findViewById<TabooEditText>(R.id.taboo_edit_text).setOnTextChangedListener { v, text, start, before, count ->
            Log.d("EditTextsActivity", "text: $text")
        }
    }
}