package com.kwon.taboo.button

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.kwon.taboo.R
import com.kwon.taboo.uicore.attribute.ButtonAppearance
import com.kwon.taboo.uicore.attribute.ColorContainer
import com.kwon.taboo.uicore.button.TabooButtonCore

open class TabooButtonButtonCore(context: Context, attrs: AttributeSet): TabooButtonCore(context, attrs) {
    private val rootView = LayoutInflater.from(context).inflate(R.layout.taboo_button, this, true)

    companion object {
        private const val ICON_POSITION_LEFT = 0
        private const val ICON_POSITION_RIGHT = 1

        private const val SIZE_LARGE = 0
        private const val SIZE_SMALL = 1
    }

    private var iconDrawable: Drawable? = null
    private var iconPosition = ICON_POSITION_LEFT

    private var size = SIZE_LARGE

    init {
        val typed = context.obtainStyledAttributes(attrs, R.styleable.TabooButton)
        val text = typed.getString(R.styleable.TabooButton_android_text) ?: ""
        val buttonShape = typed.getInt(R.styleable.TabooButton_buttonShape, ButtonAppearance.BUTTON_SHAPE_RECT)
        val buttonType = typed.getInt(R.styleable.TabooButton_buttonType, ButtonAppearance.BUTTON_TYPE_SOLID)

        val primaryColor = typed.getColor(R.styleable.TabooButton_primaryColor, ContextCompat.getColor(context, com.kwon.taboo.uicore.R.color.taboo_blue_700))
        val secondaryColor = typed.getColor(R.styleable.TabooButton_secondaryColor, ContextCompat.getColor(context, com.kwon.taboo.uicore.R.color.taboo_blue_100))
        val rippleColor = typed.getColor(R.styleable.TabooButton_rippleColor, ContextCompat.getColor(context, R.color.taboo_button_ripple_color))

        val icon = typed.getResourceId(R.styleable.TabooButton_icon, 0)
        val iconPosition = typed.getInt(R.styleable.TabooButton_iconPosition, ICON_POSITION_LEFT)
        val size = typed.getInt(R.styleable.TabooButton_size, SIZE_LARGE)

        val enabled = typed.getBoolean(R.styleable.TabooButton_android_enabled, true)

        typed.recycle()

        setText(text)

        setButtonAppearance(
            ButtonAppearance(
                context,
                buttonShape,
                buttonType,
                ColorContainer(primaryColor, secondaryColor),
                rippleColor
            )
        )

        setIconInternal(icon, iconPosition)
        setSizeInternal(size)

        isEnabled = enabled

        applyAttr()
    }

    private fun applyAttr() {
        updateText()
        updateTextColor()
        updateIcon()
        updateSize()

        drawButton()
    }

    private fun updateText() {
        rootView.findViewById<TextView>(R.id.tv_button_text).text = getText() ?: "Taboo Button"
    }

    private fun updateTextColor() {
        rootView.findViewById<TextView>(R.id.tv_button_text).setTextColor(getDefaultTextColor())
    }

    private fun getDefaultTextColor(): ColorStateList {
        val buttonAppearance = getButtonAppearance()
        val buttonType = buttonAppearance.getButtonType()
        return when (buttonType) {
            ButtonAppearance.BUTTON_TYPE_SOLID -> context.resources.getColorStateList(com.kwon.taboo.uicore.R.color.white, null)
            else -> buttonAppearance.getColorContainer().getPrimaryColorStateList()
        }
    }

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
        rootView.findViewById<ImageView>(R.id.iv_button_left_icon).apply {
            setImageDrawable(iconDrawable)
            this.visibility = visibility
        }
    }

    private fun updateRightIcon(iconDrawable: Drawable?, visibility: Int= VISIBLE) {
        rootView.findViewById<ImageView>(R.id.iv_button_right_icon).apply {
            setImageDrawable(iconDrawable)
            this.visibility = visibility
        }
    }
    // </editor-fold>

    // <editor-fold desc="Size">
    fun setSize(size: Int) {
        setSizeInternal(size)
        updateSize()
    }

    private fun setSizeInternal(size: Int) {
        this.size = size
    }

    private fun updateSize() {
        rootView.findViewById<TextView>(R.id.tv_button_text).visibility = if (size == SIZE_LARGE) VISIBLE else GONE
    }
    // </editor-fold>

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        rootView.findViewById<TextView>(R.id.tv_button_text).isEnabled = enabled
    }

    override fun drawButton() {
         background = createBackground()
    }

    override fun createBackground(): RippleDrawable {
        gradientDrawable.apply {
            cornerRadius = getRadius()
            color = getBackgroundColor()
            setStroke(getButtonStroke())
        }

        return RippleDrawable(getRippleColorState(), gradientDrawable, null)
    }
}