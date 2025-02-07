package com.kwon.taboosample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.core.content.ContextCompat
import com.kwon.taboo.button.TabooIconButton
import com.kwon.taboo.button.TabooPreviewButton
import com.kwon.taboo.edittext.TabooEditText

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btn_button_example).setOnClickListener {
            startActivity(Intent(this, ButtonsActivity::class.java))
        }

        findViewById<Button>(R.id.btn_edit_text_example).setOnClickListener {
            startActivity(Intent(this, EditTextsActivity::class.java))
        }
    }
}