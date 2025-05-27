package com.kwon.taboo.button

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.RippleDrawable
import android.util.AttributeSet
import android.util.TypedValue.COMPLEX_UNIT_PX
import android.util.TypedValue.COMPLEX_UNIT_SP
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StyleRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.content.withStyledAttributes
import com.kwon.taboo.R
import com.kwon.taboo.databinding.TabooIconButtonBinding
import com.kwon.taboo.uicore.button.TabooButtonCore

private const val ICON_POSITION_LEFT = 0
private const val ICON_POSITION_RIGHT = 1

class TabooIconButton(context: Context, attrs: AttributeSet): TabooButtonCore(context, attrs) {
    private val rootView = LayoutInflater.from(context).inflate(R.layout.taboo_icon_button, this, true)

    private var enabled = true

    private var textColorStateList: ColorStateList? = null
    private var textSize = 16f
    private var textSizeUnit = COMPLEX_UNIT_SP
    private var typeface = ResourcesCompat.getFont(context, com.kwon.taboo.uicore.R.font.font_pretendard_regular)

    @StyleRes
    private var textAppearanceResId = 0

    @DrawableRes
    private var iconDrawable = 0
    private var iconPosition = ICON_POSITION_LEFT

    init {
        context.withStyledAttributes(attrs, R.styleable.TabooIconButton) {
            background = ContextCompat.getDrawable(
                context,
                getResourceId(
                    R.styleable.TabooIconButton_android_background,
                    R.drawable.selector_taboo_icon_button
                )
            )
            setEnabled(getBoolean(R.styleable.TabooIconButton_android_enabled, true))
            setText(getString(R.styleable.TabooIconButton_android_text) ?: "")
            setTextColor(getColorStateList(R.styleable.TabooIconButton_android_textColor))
            setTypeface(getResourceId(R.styleable.TabooIconButton_android_fontFamily, 0))
            setTextSizeInternal(getDimensionPixelSize(R.styleable.TabooIconButton_android_textSize, 0))
            setIconPosition(getInt(R.styleable.TabooIconButton_iconPosition, ICON_POSITION_LEFT))
            setIconDrawable(getResourceId(R.styleable.TabooIconButton_icon, 0))

            invalidate()
        }
    }

    override fun createBackground(): RippleDrawable {
        gradientDrawable.apply {
            cornerRadius = getRadius()
            color = getBackgroundColor()
        }

        return RippleDrawable(
            getRippleColorState(),
            gradientDrawable,
            null
        )
    }

    override fun drawButton() {
        background = createBackground()
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)

        this.enabled = enabled
        updateEnabled()
    }

    private fun updateEnabled() {
        alpha = if (enabled) 1.0f else 0.3f
    }


    override fun setText(text: String) {
        super.setText(text)

        updateText()
     }

    private fun updateText() {
        rootView.findViewById<TextView>(R.id.tv_button_text).text = getText()
    }

    fun getTextColor() = textColorStateList

    fun setTextColor(color: ColorStateList?) {
        if (color == null) return

        textColorStateList = color

        updateTextColor()
    }

    fun getTextSize() = textSize

    private fun setTextSizeInternal(textSize: Int) {
        if (textSize == 0) return

        this.textSize = textSize.toFloat()
        this.textSizeUnit = COMPLEX_UNIT_PX
        updateTextSize()
    }

    fun setTextSize(textSize: Float) {
        this.textSize = textSize
        this.textSizeUnit = COMPLEX_UNIT_SP
        updateTextSize()
    }

    private fun updateTextSize() {
        rootView.findViewById<TextView>(R.id.tv_button_text).setTextSize(textSizeUnit, textSize)
    }

    fun setTextColor(@ColorRes color: Int) {
        textColorStateList = ContextCompat.getColorStateList(context, color)

        updateTextColor()
    }

    private fun updateTextColor() {
        rootView.findViewById<TextView>(R.id.tv_button_text).setTextColor(textColorStateList)
    }

    fun getTypeface() = typeface

    fun setTypeface(fontFamily: Int) {
        if (fontFamily == 0) return
        this.typeface = ResourcesCompat.getFont(context, fontFamily)
        updateTypeFace()
    }

    private fun updateTypeFace() {
        rootView.findViewById<TextView>(R.id.tv_button_text).typeface = typeface
    }

    fun setTextAppearance(@StyleRes textAppearance: Int) {
        this.textAppearanceResId = textAppearance
        updateTextAppearance()
    }

    private fun updateTextAppearance() {
        if (textAppearanceResId == 0) return

        rootView.findViewById<TextView>(R.id.tv_button_text).setTextAppearance(textAppearanceResId)
    }

    fun getIconDrawable() = iconDrawable

    fun setIconDrawable(@DrawableRes icon: Int) {
        this.iconDrawable = icon.takeIf { it != 0 } ?: getAlternativeIconDrawable()

        updateIconDrawable()
    }

    private fun updateIconDrawable() {
        val icon = ContextCompat.getDrawable(context, iconDrawable)

        if (iconPosition == ICON_POSITION_LEFT) {
            rootView.findViewById<ImageView>(R.id.iv_left_icon).setImageDrawable(icon)
        } else {
            rootView.findViewById<ImageView>(R.id.iv_right_icon).setImageDrawable(icon)
        }
    }

    /**
     * [setIconDrawable]의 매개변수가 0이라면 화면에 [iconDrawable]
     */
    @DrawableRes
    private fun getAlternativeIconDrawable() : Int {
        val isDarkTheme = AppCompatDelegate.getDefaultNightMode() == MODE_NIGHT_YES

        return if (iconPosition == ICON_POSITION_LEFT) {
            if (isDarkTheme) R.drawable.ic_round_arrow_back_e6e0e9_24dp else R.drawable.ic_round_arrow_back_191f28_24dp
        } else {
            if (isDarkTheme) R.drawable.ic_round_arrow_forward_e6e0e9_24dp else R.drawable.ic_round_arrow_forward_191f28_24dp
        }
    }

    fun getIconPosition() = iconPosition

    fun setIconPosition(iconPosition: Int) {
        this.iconPosition = iconPosition

        updateIconPosition()
    }

    private fun updateIconPosition() {
        if (iconPosition == ICON_POSITION_LEFT) {
            rootView.findViewById<ImageView>(R.id.iv_left_icon).visibility = VISIBLE
            rootView.findViewById<ImageView>(R.id.iv_right_icon).visibility = GONE
        } else {
            rootView.findViewById<ImageView>(R.id.iv_left_icon).visibility = GONE
            rootView.findViewById<ImageView>(R.id.iv_right_icon).visibility = VISIBLE
        }
    }

    override fun invalidate() {
        super.invalidate()

        updateEnabled()
        updateText()
        updateIconDrawable()
        updateIconPosition()
    }
}