package com.kwon.taboo.textview

import android.content.Context
import android.content.res.ColorStateList
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
        val iconWidth = typed.getDimension(R.styleable.TabooIconTextView_srcWidth, 36f)
        val iconHeight = typed.getDimension(R.styleable.TabooIconTextView_srcHeight, 36f)

        val text = typed.getString(R.styleable.TabooIconTextView_android_text)
        val textAppearance = typed.getResourceId(R.styleable.TabooIconTextView_android_textAppearance, 0)
        val textColor = typed.getColorStateList(R.styleable.TabooIconTextView_android_textColor)
        val textSize = typed.getDimension(R.styleable.TabooIconTextView_android_textSize, -1f)

        typed.recycle()

        setIconSrc(iconSrc)
        setIconSize(iconWidth, iconHeight)

        setText(text ?: "")
        setTextAppearance(textAppearance)
        setTextColor(textColor)
        setTextSize(textSize)
    }

    fun setIconSrc(iconSrc: Int) {
        if (iconSrc == 0) {
            return
        }
        binding.ivIcon.setImageResource(iconSrc)
    }

    fun setIconSize(width: Float, height: Float) {
        binding.ivIcon.layoutParams.width = width.toInt()
        binding.ivIcon.layoutParams.height = height.toInt()
    }

    fun setText(text: String) {
        binding.tvText.text = text
    }

    fun setTextAppearance(textAppearance: Int) {
        binding.tvText.setTextAppearance(textAppearance)
    }

    fun setTextColor(color: ColorStateList?) {
        if (color == null)
            return

        binding.tvText.setTextColor(color)
    }

    fun setTextSize(size: Float) {
        if (size == -1f)
            return

        binding.tvText.textSize = size
    }
}