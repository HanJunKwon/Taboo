package com.kwon.taboo.button

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.util.TypedValue.COMPLEX_UNIT_PX
import android.util.TypedValue.COMPLEX_UNIT_SP
import android.view.LayoutInflater
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StyleRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.kwon.taboo.R
import com.kwon.taboo.databinding.TabooIconButtonBinding

private const val ICON_POSITION_LEFT = 0
private const val ICON_POSITION_RIGHT = 1

class TabooIconButton(context: Context, attrs: AttributeSet): ConstraintLayout(context, attrs) {
    private val binding = TabooIconButtonBinding.inflate(LayoutInflater.from(context), this, true)

    private var enabled = true

    private var text = ""
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
        val typed = context.obtainStyledAttributes(attrs, R.styleable.TabooIconButton)
        val background = typed.getResourceId(R.styleable.TabooIconButton_android_background, R.drawable.selector_taboo_icon_button)
        val enabled = typed.getBoolean(R.styleable.TabooIconButton_android_enabled, true)
        val text = typed.getString(R.styleable.TabooIconButton_android_text) ?: ""
        val textColor = typed.getColorStateList(R.styleable.TabooIconButton_android_textColor)
        val textSize = typed.getDimensionPixelSize(R.styleable.TabooIconButton_android_textSize, 0)
        val fontFamily = typed.getResourceId(R.styleable.TabooIconButton_android_fontFamily, 0)
        val iconDrawable = typed.getResourceId(R.styleable.TabooIconButton_icon, 0)
        val iconPosition = typed.getInt(R.styleable.TabooIconButton_iconPosition, ICON_POSITION_LEFT)

        typed.recycle()

        setBackground(ContextCompat.getDrawable(context, background))
        setEnabled(enabled)
        setText(text)
        setTextColor(textColor)
        setTypeface(fontFamily)
        setTextSizeInternal(textSize)
        setIconPosition(iconPosition)
        setIconDrawable(iconDrawable)

        invalidate()
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)

        this.enabled = enabled
        updateEnabled()
    }

    private fun updateEnabled() {
        binding.root.alpha = if (enabled) 1.0f else 0.3f
    }

    fun getText() = text

    fun setText(text: String) {
         this.text = text
         updateText()
     }

    private fun updateText() {
        binding.tvButtonText.text = text
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
        binding.tvButtonText.setTextSize(textSizeUnit, textSize)
    }

    fun setTextColor(@ColorRes color: Int) {
        textColorStateList = ContextCompat.getColorStateList(context, color)

        updateTextColor()
    }

    private fun updateTextColor() {
        binding.tvButtonText.setTextColor(textColorStateList)
    }

    fun getTypeface() = typeface

    fun setTypeface(fontFamily: Int) {
        if (fontFamily == 0) return
        this.typeface = ResourcesCompat.getFont(context, fontFamily)
        updateTypeFace()
    }

    private fun updateTypeFace() {
        binding.tvButtonText.typeface = typeface
    }

    fun setTextAppearance(@StyleRes textAppearance: Int) {
        this.textAppearanceResId = textAppearance
        updateTextAppearance()
    }

    private fun updateTextAppearance() {
        if (textAppearanceResId == 0) return

        binding.tvButtonText.setTextAppearance(textAppearanceResId)
    }

    fun getIconDrawable() = iconDrawable

    fun setIconDrawable(@DrawableRes icon: Int) {
        this.iconDrawable = icon.takeIf { it != 0 } ?: getAlternativeIconDrawable()

        updateIconDrawable()
    }

    private fun updateIconDrawable() {
        val icon = ContextCompat.getDrawable(context, iconDrawable)

        if (iconPosition == ICON_POSITION_LEFT) {
            binding.ivLeftIcon.setImageDrawable(icon)
        } else {
            binding.ivRightIcon.setImageDrawable(icon)
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
            binding.ivLeftIcon.visibility = VISIBLE
            binding.ivRightIcon.visibility = GONE
        } else {
            binding.ivLeftIcon.visibility = GONE
            binding.ivRightIcon.visibility = VISIBLE
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