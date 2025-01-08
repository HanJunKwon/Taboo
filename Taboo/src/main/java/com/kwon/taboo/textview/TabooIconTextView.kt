package com.kwon.taboo.textview

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.kwon.taboo.R
import com.kwon.taboo.databinding.TabooIconTextViewBinding

class TabooIconTextView(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {
    private val binding = TabooIconTextViewBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        val typed = context.obtainStyledAttributes(attrs, R.styleable.TabooIconTextView)

        val iconSrc = typed.getResourceId(R.styleable.TabooIconTextView_android_src, 0)

        val text = typed.getString(R.styleable.TabooIconTextView_android_text)
        val textAppearance = typed.getResourceId(R.styleable.TabooIconTextView_android_textAppearance, 0)
        val textColor = typed.getColorStateList(R.styleable.TabooIconTextView_android_textColor)
        val textSize = typed.getDimension(R.styleable.TabooIconTextView_android_textSize, 14f)

        typed.recycle()

        setIconSrc(iconSrc)

        setText(text ?: "")
        setTextAppearance(textAppearance)
        setTextColor(textColor?.defaultColor ?: Color.BLACK)
        setTextSize(textSize)
    }

    fun setIconSrc(iconSrc: Int) {
        if (iconSrc == 0) {
            return
        }
        binding.ivIcon.setImageResource(iconSrc)
    }

    fun setText(text: String) {
        binding.tvText.text = text
    }

    fun setTextAppearance(textAppearance: Int) {
        binding.tvText.setTextAppearance(textAppearance)
    }

    fun setTextColor(color: Int) {
        binding.tvText.setTextColor(color)
    }

    fun setTextSize(size: Float) {
        binding.tvText.textSize = size
    }
}