package com.kwon.taboo.textview

import android.content.Context
import android.content.res.ColorStateList
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.FontRes
import androidx.annotation.StyleRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.TextViewCompat
import com.kwon.taboo.R
import com.kwon.taboo.uicore.util.ResourceUtils

class TabooIconTextView(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {
    private val rootView = LayoutInflater.from(context).inflate(R.layout.taboo_icon_text_view, this, true)

    private var space = 0

    init {
        val typed = context.obtainStyledAttributes(attrs, R.styleable.TabooIconTextView)

        val iconSrc = typed.getResourceId(R.styleable.TabooIconTextView_android_src, 0)
        val iconWidth = typed.getDimension(R.styleable.TabooIconTextView_srcWidth, -1f)
        val iconHeight = typed.getDimension(R.styleable.TabooIconTextView_srcHeight, -1f)

        val text = typed.getString(R.styleable.TabooIconTextView_android_text)
        val textAppearance = typed.getResourceId(R.styleable.TabooIconTextView_android_textAppearance, 0)
        val textColor = typed.getColorStateList(R.styleable.TabooIconTextView_android_textColor)
        val textSize = typed.getDimension(R.styleable.TabooIconTextView_android_textSize, DEFAULT_TEXT_SIZE)
        val typeFace = typed.getResourceId(R.styleable.TabooIconTextView_android_fontFamily, com.kwon.taboo.uicore.R.font.font_pretendard_regular)

        val maxLines = typed.getInt(R.styleable.TabooIconTextView_android_maxLines, 1)
        val ellipsize = typed.getInt(R.styleable.TabooIconTextView_android_ellipsize, 0)

        val space = typed.getDimensionPixelSize(R.styleable.TabooIconTextView_space, ResourceUtils.spToPx(context, 6f).toInt())

        typed.recycle()

        setIconSrc(iconSrc)
        setIconSize(iconWidth.toInt(), iconHeight.toInt())

        setText(text ?: "")
        setTextAppearance(textAppearance)
        setTextColor(textColor)
        setTextSize(textSize)
        setTypeFace(typeFace)

        setMaxLines(maxLines)
        setEllipsize(ellipsize)

        setSpace(space)
    }

    fun setIconSrc(iconSrc: Int) {
        if (iconSrc == 0) {
            return
        }
        rootView.findViewById<ImageView>(R.id.iv_icon).setImageResource(iconSrc)
    }

    fun setIconSize(width: Int, height: Int) {
        rootView.findViewById<ImageView>(R.id.iv_icon).apply {
            this.layoutParams = this.layoutParams.apply {
                this.width = if (width == -1) LayoutParams.WRAP_CONTENT else width
                this.height = if (height == -1) LayoutParams.WRAP_CONTENT else height
            }
        }
    }

    fun setText(text: String) {
        rootView.findViewById<TextView>(R.id.tv_text).text = text
    }

    fun setTextAppearance(@StyleRes textAppearance: Int) {
        TextViewCompat.setTextAppearance(
            rootView.findViewById<TextView>(R.id.tv_button_text),
            textAppearance
        )
    }

    fun setTextColor(color: ColorStateList?) {
        if (color == null)
            return

        rootView.findViewById<TextView>(R.id.tv_text).setTextColor(color)
    }

    fun setTextSize(size: Float) {
        rootView.findViewById<TextView>(R.id.tv_text).setTextSize(TypedValue.COMPLEX_UNIT_PX, size)
    }

    fun setTypeFace(@FontRes typeFace: Int) {
        rootView.findViewById<TextView>(R.id.tv_text).typeface = ResourcesCompat.getFont(context, typeFace)
    }

    fun setMaxLines(maxLines: Int) {
        rootView.findViewById<TextView>(R.id.tv_text).maxLines = maxLines
    }

    fun setEllipsize(ellipsize: Int) {
        rootView.findViewById<TextView>(R.id.tv_text).ellipsize = when (ellipsize) {
            1 -> android.text.TextUtils.TruncateAt.START
            2 -> android.text.TextUtils.TruncateAt.MIDDLE
            3 -> android.text.TextUtils.TruncateAt.END
            else -> android.text.TextUtils.TruncateAt.END
        }
    }

    fun setSpace(space: Int) {
        this.space = space

        updateSpace()
    }

    fun updateSpace() {
        if (space == 0) {
            return
        }

        (rootView.findViewById<TextView>(R.id.tv_text).layoutParams as MarginLayoutParams).apply {
            marginStart = space
        }
    }

    companion object {
        const val DEFAULT_TEXT_SIZE = 32f
    }
}