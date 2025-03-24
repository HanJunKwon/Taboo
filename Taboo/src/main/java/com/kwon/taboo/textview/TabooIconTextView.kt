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
        val iconWidth = typed.getDimension(R.styleable.TabooIconTextView_srcWidth, -1f)
        val iconHeight = typed.getDimension(R.styleable.TabooIconTextView_srcHeight, -1f)

        val text = typed.getString(R.styleable.TabooIconTextView_android_text)
        val textAppearance = typed.getResourceId(R.styleable.TabooIconTextView_android_textAppearance, 0)
        val textColor = typed.getColorStateList(R.styleable.TabooIconTextView_android_textColor)
        val textSize = typed.getDimension(R.styleable.TabooIconTextView_android_textSize, -1f)

        val maxLines = typed.getInt(R.styleable.TabooIconTextView_android_maxLines, 1)
        val ellipsize = typed.getInt(R.styleable.TabooIconTextView_android_ellipsize, 0)

        typed.recycle()

        setIconSrc(iconSrc)
        setIconSize(iconWidth.toInt(), iconHeight.toInt())

        setText(text ?: "")
        setTextAppearance(textAppearance)
        setTextColor(textColor)
        setTextSize(textSize)

        setMaxLines(maxLines)
        setEllipsize(ellipsize)
    }

    fun setIconSrc(iconSrc: Int) {
        if (iconSrc == 0) {
            return
        }
        binding.ivIcon.setImageResource(iconSrc)
    }

    fun setIconSize(width: Int, height: Int) {
        val layoutParams = binding.ivIcon.layoutParams.apply {
            this.width = if (width == -1) LayoutParams.WRAP_CONTENT else width
            this.height = if (height == -1) LayoutParams.WRAP_CONTENT else height
        }

        binding.ivIcon.layoutParams = layoutParams
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

    fun setMaxLines(maxLines: Int) {
        binding.tvText.maxLines = maxLines
    }

    fun setEllipsize(ellipsize: Int) {
        binding.tvText.ellipsize = when (ellipsize) {
            1 -> android.text.TextUtils.TruncateAt.START
            2 -> android.text.TextUtils.TruncateAt.MIDDLE
            3 -> android.text.TextUtils.TruncateAt.END
            else -> android.text.TextUtils.TruncateAt.END
        }
    }
}