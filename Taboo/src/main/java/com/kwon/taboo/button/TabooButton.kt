package com.kwon.taboo.button

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.kwon.taboo.R
import com.kwon.taboo.databinding.TabooButtonBinding

class TabooButton(context: Context, attrs: AttributeSet): ConstraintLayout(context, attrs) {
    private val binding = TabooButtonBinding.inflate(LayoutInflater.from(context), this, true)

    companion object {
        private const val BUTTON_SHAPE_RECT = 0
        private const val BUTTON_SHAPE_ROUNDED = 1
    }

    private var text = ""
    private var buttonShape = 0

    init {
        val typed = context.obtainStyledAttributes(attrs, R.styleable.TabooButton)
        val text = typed.getString(R.styleable.TabooButton_android_text) ?: ""
        val buttonShape = typed.getInt(R.styleable.TabooButton_buttonShape, BUTTON_SHAPE_RECT)

        typed.recycle()

        setText(text)
        setButtonShape(buttonShape)
    }

    fun setText(text: String) {
        this.text = text
        updateText()
    }

    private fun updateText() {
        binding.tvButtonText.text = text
    }

    fun setButtonShape(shape: Int) {
        this.buttonShape = shape
        updateButtonShape()
    }

    private fun updateButtonShape() {
        val backgroundDrawable = when (this.buttonShape) {
            BUTTON_SHAPE_RECT -> R.drawable.shape_rect_r0_a100_1731e9
            BUTTON_SHAPE_ROUNDED -> R.drawable.shape_rect_r15_a100_1731e9
            else -> 0
        }

        binding.clButtonWrapper.background = ContextCompat.getDrawable(context, backgroundDrawable)
    }
}