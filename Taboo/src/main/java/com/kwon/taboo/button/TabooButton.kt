package com.kwon.taboo.button

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
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
    private var textColor: ColorStateList? = null
    private var buttonShape = 0

    init {
        val typed = context.obtainStyledAttributes(attrs, R.styleable.TabooButton)
        val text = typed.getString(R.styleable.TabooButton_android_text) ?: ""
        val textColor = typed.getColorStateList(R.styleable.TabooButton_android_textColor)
        val buttonShape = typed.getInt(R.styleable.TabooButton_buttonShape, BUTTON_SHAPE_RECT)
        val buttonBackgroundTint = typed.getColorStateList(R.styleable.TabooButton_buttonColor)

        typed.recycle()

        setText(text)
        setTextColor(textColor)
        setButtonShape(buttonShape)
        setButtonBackgroundTint(buttonBackgroundTint)
    }

    fun setText(text: String) {
        this.text = text
        updateText()
    }

    private fun updateText() {
        binding.tvButtonText.text = text
    }

    fun setTextColor(textColor: ColorStateList?) {
        this.textColor = textColor ?: ContextCompat.getColorStateList(context, R.color.white)
        updateTextColor()
    }

    private fun updateTextColor() {
        binding.tvButtonText.setTextColor(textColor)
    }

    fun setButtonShape(shape: Int) {
        this.buttonShape = shape
        updateButtonShape()
    }

    private fun updateButtonShape() {
        val backgroundDrawable = when (this.buttonShape) {
            BUTTON_SHAPE_RECT -> R.drawable.shape_rect_r0_a100_0047ff
            BUTTON_SHAPE_ROUNDED -> R.drawable.shape_rect_r15_a100_0047ff
            else -> 0
        }

        binding.clButtonWrapper.background = ContextCompat.getDrawable(context, backgroundDrawable)
    }

    fun setButtonBackgroundTint(color: Any?) {
        val background = binding.clButtonWrapper.background as? GradientDrawable
        if (background != null) {
            when (color) {
                is Int -> background.setColor(color)            // 단일 색상 처리
                is ColorStateList -> background.color = color   // ColorStateList 처리
                else -> background.setColor(ContextCompat.getColor(context, R.color.taboo_vibrant_blue_01))              // 기본값
            }
        }
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        binding.clButtonWrapper.isEnabled = enabled
    }

}