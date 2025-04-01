package com.kwon.taboo.button

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.kwon.taboo.R
import com.kwon.taboo.attribute.ButtonAppearance
import com.kwon.taboo.attribute.ColorContainer
import com.kwon.taboo.databinding.TabooButtonBinding

class TabooButton(context: Context, attrs: AttributeSet): ConstraintLayout(context, attrs) {
    private val binding = TabooButtonBinding.inflate(LayoutInflater.from(context), this, true)

    companion object {
        const val BUTTON_SHAPE_RECT = 0
        const val BUTTON_SHAPE_ROUNDED = 1

        const val BUTTON_TYPE_SOLID = 0
        const val BUTTON_TYPE_FILL = 1
        const val BUTTON_TYPE_OUTLINE = 2
        const val BUTTON_TYPE_DASH = 3

        private const val ICON_POSITION_LEFT = 0
        private const val ICON_POSITION_RIGHT = 1

        private const val SIZE_LARGE = 0
        private const val SIZE_SMALL = 1
    }

    private var text = ""
    private var textColor: Any? = null
    private var buttonShape = BUTTON_SHAPE_RECT
    private var buttonType = BUTTON_TYPE_SOLID

    private var colorContainer: ColorContainer = ColorContainer(
        primaryColor = ContextCompat.getColor(context, R.color.taboo_vibrant_blue_01),
        secondaryColor = ContextCompat.getColor(context, R.color.taboo_blue_06)
    )

    @ColorInt private var rippleColor: Int = ContextCompat.getColor(context, R.color.taboo_button_ripple_color)

    private var iconDrawable: Drawable? = null
    private var iconPosition = ICON_POSITION_LEFT

    private var size = SIZE_LARGE

    init {
        val typed = context.obtainStyledAttributes(attrs, R.styleable.TabooButton)
        val text = typed.getString(R.styleable.TabooButton_android_text) ?: ""
        val textColor = typed.getColorStateList(R.styleable.TabooButton_android_textColor)
        val buttonShape = typed.getInt(R.styleable.TabooButton_buttonShape, BUTTON_SHAPE_RECT)
        val buttonType = typed.getInt(R.styleable.TabooButton_buttonType, BUTTON_TYPE_SOLID)

        val primaryColor = typed.getColor(R.styleable.TabooButton_primaryColor, ContextCompat.getColor(context, R.color.taboo_vibrant_blue_01))
        val secondaryColor = typed.getColor(R.styleable.TabooButton_secondaryColor, ContextCompat.getColor(context, R.color.taboo_blue_06))
        val rippleColor = typed.getColor(R.styleable.TabooButton_rippleColor, 0)

        val icon = typed.getResourceId(R.styleable.TabooButton_icon, 0)
        val iconPosition = typed.getInt(R.styleable.TabooButton_iconPosition, ICON_POSITION_LEFT)
        val size = typed.getInt(R.styleable.TabooButton_size, SIZE_LARGE)

        val enabled = typed.getBoolean(R.styleable.TabooButton_android_enabled, true)

        typed.recycle()

        setText(text)
        setTextColorInternal(textColor)

        setButtonShapeInternal(buttonShape)
        setButtonTypeInternal(buttonType)

        setColorContainerInternal(ColorContainer(primaryColor, secondaryColor))
        setRippleColorInternal(rippleColor)

        setIconInternal(icon, iconPosition)
        setSizeInternal(size)

        isEnabled = enabled

        applyAttr()
    }

    private fun applyAttr() {
        updateTextColor()
        updateIcon()
        updateSize()

        drawButton()

        isClickable = true
    }

    fun getText() = text

    fun setText(text: String) {
        this.text = text
        updateText()
    }

    private fun updateText() {
        binding.tvButtonText.text = text
    }

    fun getTextColors() = textColor

    fun setTextColor(textColor: ColorStateList?) {
        setTextColorInternal(textColor)
        updateTextColor()
    }

    private fun setTextColorInternal(textColor: ColorStateList?) {
        this.textColor = textColor
    }

    private fun updateTextColor() {
        if (textColor == null) {
            binding.tvButtonText.setTextColor(getDefaultTextColor(buttonType))
        } else {
            if (textColor is Int) {
                binding.tvButtonText.setTextColor(textColor as Int)
            } else {
                binding.tvButtonText.setTextColor(textColor as ColorStateList)
            }
        }
    }

    private fun getDefaultTextColor(buttonType: Int): ColorStateList {
        return when (buttonType) {
            BUTTON_TYPE_SOLID -> context.resources.getColorStateList(R.color.white, null)
            else -> colorContainer.getPrimaryColorStateList()
        }
    }

    fun getButtonShape() = buttonShape

    fun setButtonShape(shape: Int) {
        setButtonShapeInternal(shape)
    }

    private fun setButtonShapeInternal(shape: Int) {
        this.buttonShape = shape
    }

    fun getButtonType() = buttonType

    fun setButtonType(type: Int) {
        setButtonTypeInternal(type)
    }

    private fun setButtonTypeInternal(type: Int) {
        this.buttonType = type
    }

    // <editor-fold desc="Color">

    private fun setColorContainerInternal(colorContainer: ColorContainer) {
        this.colorContainer = colorContainer
    }

    private fun setRippleColorInternal(@ColorInt rippleColor: Int) {
        if (rippleColor != 0) {
            this.rippleColor = rippleColor
        }
    }

    fun getPrimaryColor() = colorContainer.primaryColor

    fun setPrimaryColor(@ColorInt primaryColor: Int) {
        colorContainer.primaryColor = primaryColor
        drawButton()
    }

    fun getSecondaryColor() = colorContainer.secondaryColor

    fun setSecondaryColor(@ColorInt secondaryColor: Int) {
        colorContainer.secondaryColor = secondaryColor
        drawButton()
    }

    fun getRippleColor() = rippleColor

    /**
     * 버튼 클릭 시 발생하는 Ripple 효과의 Mask 색상을 지정한다.
     *
     * [rippleColor]가 0이면 기본 Ripple 색상인 [R.color.taboo_button_ripple_color]가 적용된다.
     */
    fun setRippleColor(@ColorInt rippleColor: Int) {
        setRippleColorInternal(rippleColor)

        drawButton()
    }

    // </editor-fold>

    // <editor-fold desc="Icon">
    fun getIcon() = iconDrawable

    fun setIcon(@DrawableRes icon: Int, position: Int = ICON_POSITION_LEFT) {
        setIconInternal(icon,position)
        updateIcon()
    }

    private fun setIconInternal(@DrawableRes icon: Int, position: Int = ICON_POSITION_LEFT) {
        this.iconDrawable = if (icon == 0) null else ContextCompat.getDrawable(context, icon)
        this.iconPosition = position
    }

    fun getIconPosition() = iconPosition

    private fun updateIcon() {
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

    private fun updateLeftIcon(iconDrawable: Drawable?, visibility: Int= VISIBLE) {
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

    private fun drawButton() {
        val gradientDrawable = ButtonAppearance(context, buttonShape, buttonType, colorContainer, rippleColor).create()

        binding.root.background = gradientDrawable
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        binding.root.isEnabled = enabled
        binding.tvButtonText.isEnabled = enabled
    }

}