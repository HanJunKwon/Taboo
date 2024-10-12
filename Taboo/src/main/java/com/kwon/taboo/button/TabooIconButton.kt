package com.kwon.taboo.button

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue.COMPLEX_UNIT_PX
import android.util.TypedValue.COMPLEX_UNIT_SP
import android.view.LayoutInflater
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.kwon.taboo.R
import com.kwon.taboo.databinding.TabooIconButtonBinding

private const val ICON_POSITION_LEFT = 0
private const val ICON_POSITION_RIGHT = 1

class TabooIconButton(context: Context, attrs: AttributeSet): ConstraintLayout(context, attrs) {
    private val binding = TabooIconButtonBinding.inflate(LayoutInflater.from(context), this, true)

    private var enabled = true

    private var text = ""
    private var textColorStateList: ColorStateList? = null

    @DrawableRes
    private var iconDrawable = 0
    private var iconPosition = ICON_POSITION_LEFT

    init {
        val typed = context.obtainStyledAttributes(attrs, R.styleable.TabooIconButton)
        val enabled = typed.getBoolean(R.styleable.TabooIconButton_android_enabled, true)
        val text = typed.getString(R.styleable.TabooIconButton_android_text) ?: ""
        val textColor = typed.getColorStateList(R.styleable.TabooIconButton_android_textColor)
        val iconDrawable = typed.getResourceId(R.styleable.TabooIconButton_icon, 0)
        val iconPosition = typed.getInt(R.styleable.TabooIconButton_iconPosition, ICON_POSITION_LEFT)

        typed.recycle()

        setEnabled(enabled)
        setText(text)
        setTextColor(textColor)
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

    fun setText(text: String) {
         this.text = text
         updateText()
     }

    private fun updateText() {
        binding.tvButtonText.text = text
    }

    fun setTextColor(color: ColorStateList?) {
        if (color == null) return

        textColorStateList = color

        updateTextColor()
    }

    fun setTextColor(@ColorRes color: Int) {
        textColorStateList = ContextCompat.getColorStateList(context, color)

        updateTextColor()
    }

    private fun updateTextColor() {
        binding.tvButtonText.setTextColor(textColorStateList)
    }

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