package com.kwon.taboosample

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.kwon.taboosample.databinding.ActivityEditTextsBinding
import com.kwon.taboosample.viewmodel.TextViewModel

class EditTextsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditTextsBinding
    private val textViewModel = TextViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_texts)
        enableEdgeToEdge()
        setContentView(binding.root)

        binding.lifecycleOwner = this
        binding.textViewModel = textViewModel

//        findViewById<TabooTextInput>(R.id.taboo_edit_text).apply {
//            setOnEditTextChangedListener { text, start, before, count ->
//                Log.d(">>>", "$text, $start, $before, $count")
//            }
//        }
//
//        findViewById<TabooTextInput>(R.id.taboo_drop_down).apply {
//            setDropdownItems(listOf("대한민국", "일본", "미국"))
//            setDropdownItemSelectedListener { parent, view, position, id ->
//                 Log.d(">>>", "selectedListener() :: position: $position")
//            }
//
//            setDropdownItemChangedListener { position ->
//                Log.d(">>>", "changeListener() :: position: $position")
//            }
//
//            setDropdownSelectedPosition(-2)
//        }
//
//        findViewById<TabooButton>(R.id.btn_ok).setOnClickListener {
//            findViewById<TabooTextInput>(R.id.taboo_edit_text).isError(true)
//
//            findViewById<TabooTextInput>(R.id.taboo_drop_down).setDropdownItems(listOf("010", "011", "019", "012"))
//            findViewById<TabooTextInput>(R.id.taboo_drop_down).setDropdownSelectedPosition(2)
//        }
    }
}