package com.kwon.taboo.button

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.kwon.taboo.R
import com.kwon.taboo.databinding.TabooButtonBinding

class TabooButton(context: Context, attrs: AttributeSet): ConstraintLayout(context, attrs) {
    private val binding = TabooButtonBinding.inflate(LayoutInflater.from(context), this, true)

    companion object {
        private const val BUTTON_SHAPE_RECT = 0
        private const val BUTTON_SHAPE_ROUNDED = 1

        private const val ICON_POSITION_LEFT = 0
        private const val ICON_POSITION_RIGHT = 1

        private const val SIZE_LARGE = 0
        private const val SIZE_SMALL = 1
    }

    private var isAttrApplying = false

    private var text = ""
    private var textColor: ColorStateList? = null
    private var buttonShape = 0

    private var size = SIZE_LARGE

    init {
        val typed = context.obtainStyledAttributes(attrs, R.styleable.TabooButton)
        val text = typed.getString(R.styleable.TabooButton_android_text) ?: ""
        val textColor = typed.getColorStateList(R.styleable.TabooButton_android_textColor)
        val buttonShape = typed.getInt(R.styleable.TabooButton_buttonShape, BUTTON_SHAPE_RECT)
        val buttonBackgroundTint = typed.getColorStateList(R.styleable.TabooButton_buttonColor)
        val icon = typed.getResourceId(R.styleable.TabooButton_icon, 0)
        val iconPosition = typed.getInt(R.styleable.TabooButton_iconPosition, ICON_POSITION_LEFT)
        val size = typed.getInt(R.styleable.TabooButton_size, SIZE_LARGE)

        typed.recycle()

        setText(text)
        setTextColor(textColor)
        setButtonShape(buttonShape)
        setButtonBackgroundTint(buttonBackgroundTint)
        setIcon(icon, iconPosition)
        setSizeInternal(size)

        applyAttr()
    }

    private fun applyAttr() {
        if (!isAttrApplying) {
            isAttrApplying = true
            post {
                updateSize()
                isAttrApplying = false
            }
        }
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

    // <editor-fold desc="Icon">
    private fun setIcon(@DrawableRes icon: Int, position: Int = ICON_POSITION_LEFT) {
        when {
            icon == 0 -> {
                setLeftIcon(null, GONE)
                setRightIcon(null, GONE)
            }
            position == ICON_POSITION_RIGHT -> {
                setLeftIcon(null, GONE)
                setRightIcon(ContextCompat.getDrawable(context, icon))
            }
            else -> {
                setRightIcon(null, GONE)
                setLeftIcon(ContextCompat.getDrawable(context, icon))
            }
        }
    }

    private fun setLeftIcon(iconDrawable: Drawable?, visibility: Int= VISIBLE) {
        binding.ivButtonLeftIcon.setImageDrawable(iconDrawable)
        binding.ivButtonLeftIcon.visibility = visibility
    }

    private fun setRightIcon(iconDrawable: Drawable?, visibility: Int= VISIBLE) {
        binding.ivButtonRightIcon.setImageDrawable(iconDrawable)
        binding.ivButtonRightIcon.visibility = visibility
    }

    // </editor-fold>

    private fun setSizeInternal(size: Int) {
        this.size = size
    }

    private fun updateSize() {
        binding.tvButtonText.visibility = if (size == SIZE_LARGE) VISIBLE else GONE
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        binding.clButtonWrapper.isEnabled = enabled
    }

}