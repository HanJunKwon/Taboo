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

        private const val BUTTON_TYPE_SOLID = 0
        private const val BUTTON_TYPE_FILL = 1
        private const val BUTTON_TYPE_OUTLINE = 2
        private const val BUTTON_TYPE_DASH = 3

        private const val ICON_POSITION_LEFT = 0
        private const val ICON_POSITION_RIGHT = 1

        private const val SIZE_LARGE = 0
        private const val SIZE_SMALL = 1
    }

    private var isAttrApplying = false

    private var text = ""
    private var textColor: ColorStateList? = null
    private var buttonShape = 0
    private var buttonType = BUTTON_TYPE_SOLID

    private var backgroundTint: Any? = null

    private var iconDrawable: Drawable? = null
    private var iconPosition = ICON_POSITION_LEFT

    private var size = SIZE_LARGE

    init {
        val typed = context.obtainStyledAttributes(attrs, R.styleable.TabooButton)
        val text = typed.getString(R.styleable.TabooButton_android_text) ?: ""
        val textColor = typed.getColorStateList(R.styleable.TabooButton_android_textColor)
        val buttonShape = typed.getInt(R.styleable.TabooButton_buttonShape, BUTTON_SHAPE_RECT)
        val buttonType = typed.getInt(R.styleable.TabooButton_buttonType, BUTTON_TYPE_SOLID)
        val buttonBackgroundTint = typed.getColorStateList(R.styleable.TabooButton_buttonBackgroundTint)
        val icon = typed.getResourceId(R.styleable.TabooButton_icon, 0)
        val iconPosition = typed.getInt(R.styleable.TabooButton_iconPosition, ICON_POSITION_LEFT)
        val size = typed.getInt(R.styleable.TabooButton_size, SIZE_LARGE)

        typed.recycle()

        setText(text)
        setTextColor(textColor)
        setButtonShapeInternal(buttonShape)
        setButtonTypeInternal(buttonType)
        setButtonBackgroundTintInternal(buttonBackgroundTint)
        setIconInternal(icon, iconPosition)
        setSizeInternal(size)

        applyAttr()
    }

    private fun applyAttr() {
        updateButtonShape()
        updateButtonBackgroundTint(backgroundTint)
        updateIcon()
        updateSize()
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
        setButtonShapeInternal(shape)
        updateButtonShape()
    }

    private fun setButtonShapeInternal(shape: Int) {
        this.buttonShape = shape
    }

    private fun updateButtonShape() {
        val backgroundDrawable = when (this.buttonShape) {
            BUTTON_SHAPE_RECT -> R.drawable.shape_rect_r0_a0_000000
            BUTTON_SHAPE_ROUNDED -> R.drawable.shape_rect_r15_a0_000000
            else -> 0
        }

        binding.clButtonWrapper.background = ContextCompat.getDrawable(context, backgroundDrawable)
    }

    fun setButtonType(type: Int) {
        setButtonTypeInternal(type)
    }

    private fun setButtonTypeInternal(type: Int) {
        this.buttonType = type
    }

    fun setButtonBackgroundTint(backgroundTint: Any?) {
        setButtonBackgroundTintInternal(backgroundTint)
        updateButtonBackgroundTint(backgroundTint)
    }

    private fun setButtonBackgroundTintInternal(color: Any?) {
        backgroundTint = color
    }

    private fun updateButtonBackgroundTint(color: Any?) {
        val background = binding.clButtonWrapper.background as? GradientDrawable

        if (backgroundTint == null) {
            // 기본 색상 적용
            background?.color = ContextCompat.getColorStateList(context, getDefaultBackgroundColor(buttonType))
        } else {
            // 지정한 색상 적용
            if (background != null) {
                when (color) {
                    is Int -> background.setColor(ContextCompat.getColor(context, color))                           // 단일 색상 처리
                    is ColorStateList -> background.color = color                                                   // ColorStateList 처리
                    else -> background.setColor(ContextCompat.getColor(context, R.color.taboo_vibrant_blue_01))     // 기본값
                }
            }
        }
    }

    private fun getDefaultBackgroundColor(buttonType: Int): Int {
        return when (buttonType) {
            BUTTON_TYPE_SOLID -> R.color.selector_taboo_button_solid_type_background_color
            BUTTON_TYPE_FILL -> R.color.selector_taboo_button_fill_type_background_color
            BUTTON_TYPE_OUTLINE -> R.color.selector_taboo_button_outline_type_background_color
            BUTTON_TYPE_DASH -> R.color.selector_taboo_button_dash_type_background_color
            else -> R.color.selector_taboo_button_solid_type_background_color
        }
    }

    // <editor-fold desc="Icon">
    fun setIcon(@DrawableRes icon: Int, position: Int = ICON_POSITION_LEFT) {
        setIconInternal(icon,position)
        updateIcon()
    }

    private fun setIconInternal(@DrawableRes icon: Int, position: Int = ICON_POSITION_LEFT) {
        this.iconDrawable = if (icon == 0) null else ContextCompat.getDrawable(context, icon)
        this.iconPosition = position
    }

    private fun updateIcon() {
        Log.d(">>>", "$iconDrawable")
        when {
            iconDrawable == null -> {
                updateLeftIcon(null, GONE)
                updateRightIcon(null, GONE)
            }
            iconPosition == ICON_POSITION_RIGHT -> {
                updateLeftIcon(null, GONE)
                updateRightIcon(iconDrawable)
            }
            else -> {
                updateRightIcon(null, GONE)
                updateLeftIcon(iconDrawable)
            }
        }
    }

    private fun  updateLeftIcon(iconDrawable: Drawable?, visibility: Int= VISIBLE) {
        binding.ivButtonLeftIcon.setImageDrawable(iconDrawable)
        binding.ivButtonLeftIcon.visibility = visibility
    }

    private fun updateRightIcon(iconDrawable: Drawable?, visibility: Int= VISIBLE) {
        binding.ivButtonRightIcon.setImageDrawable(iconDrawable)
        binding.ivButtonRightIcon.visibility = visibility
    }

    // </editor-fold>

    fun setSize(size: Int) {
        setSizeInternal(size)
        updateSize()
    }

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