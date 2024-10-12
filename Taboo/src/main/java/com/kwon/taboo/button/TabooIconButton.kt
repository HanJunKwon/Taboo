package com.kwon.taboo.button

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.kwon.taboo.R
import com.kwon.taboo.databinding.TabooIconButtonBinding

class TabooIconButton(context: Context, attrs: AttributeSet): ConstraintLayout(context, attrs) {
    private val binding = TabooIconButtonBinding.inflate(LayoutInflater.from(context), this, true)

    private var enabled = true

    private var text = ""

    init {
        val typed = context.obtainStyledAttributes(attrs, R.styleable.TabooIconButton)
        val enabled = typed.getBoolean(R.styleable.TabooIconButton_android_enabled, true)
        val text = typed.getString(R.styleable.TabooIconButton_android_text) ?: ""

        typed.recycle()

        setEnabled(enabled)
        setText(text)
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)

        this.enabled = enabled
        updateEnabled()
    }

    private fun updateEnabled() {
        binding.root.alpha = if (enabled) 1.0f else 0.3f
    }

    fun setText(text: String) {
         this.text = text
         updateText()
     }

    private fun updateText() {
        binding.tvButtonText.text = text
    }
}