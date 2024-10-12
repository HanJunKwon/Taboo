package com.kwon.taboo.button

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.kwon.taboo.R
import com.kwon.taboo.databinding.TabooIconButtonBinding

private const val ICON_POSITION_LEFT = 0
private const val ICON_POSITION_RIGHT = 1

class TabooIconButton(context: Context, attrs: AttributeSet): ConstraintLayout(context, attrs) {
    private val binding = TabooIconButtonBinding.inflate(LayoutInflater.from(context), this, true)

    private var enabled = true

    private var text = ""

    private var iconPosition = ICON_POSITION_LEFT

    init {
        val typed = context.obtainStyledAttributes(attrs, R.styleable.TabooIconButton)
        val enabled = typed.getBoolean(R.styleable.TabooIconButton_android_enabled, true)
        val text = typed.getString(R.styleable.TabooIconButton_android_text) ?: ""
        val iconPosition = typed.getInt(R.styleable.TabooIconButton_iconPosition, ICON_POSITION_LEFT)

        typed.recycle()

        setEnabled(enabled)
        setText(text)
        setIconPosition(iconPosition)
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
}