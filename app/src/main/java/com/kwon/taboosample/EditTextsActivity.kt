package com.kwon.taboosample

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.kwon.taboo.textfield.TabooTextInput

class EditTextsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_texts)

        findViewById<TabooTextInput>(R.id.taboo_edit_text).apply {
            setOnEditTextChangedListener { text, start, before, count ->
                Log.d(">>>", "$text, $start, $before, $count")
            }
        }

        findViewById<TabooTextInput>(R.id.taboo_drop_down).apply {
            setDropdownItems(arrayOf("대한민국", "일본", "미국"))
            setDropdownItemSelectedListener { parent, view, position, id ->
                 Log.d(">>>", "selectedListener() :: position: $position")
            }

            setDropdownItemChangedListener { position ->
                Log.d(">>>", "changeListener() :: position: $position")
            }

            setDropdownSelectedPosition(-2)
        }
    }
}