package com.kwon.taboo.button

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.kwon.taboo.R
import com.kwon.taboo.databinding.TabooIconButtonBinding

class TabooIconButton(context: Context, attrs: AttributeSet): ConstraintLayout(context, attrs) {
    private val binding = TabooIconButtonBinding.inflate(LayoutInflater.from(context), this, true)

    private var text = ""

    init {
        val typed = context.obtainStyledAttributes(attrs, R.styleable.TabooIconButton)
        val text = typed.getString(R.styleable.TabooIconButton_android_text) ?: ""

        typed.recycle()

        setText(text)
    }

     fun setText(text: String) {
         this.text = text
         updateText()
     }

    private fun updateText() {
        binding.tvButtonText.text = text
    }
}