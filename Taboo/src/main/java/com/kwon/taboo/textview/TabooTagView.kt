package com.kwon.taboo.textview

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.util.AttributeSet
import android.view.Gravity
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.kwon.taboo.R

private const val TAG_VIEW_PADDING_HORIZONTAL = 10
private const val TAG_VIEW_PADDING_VERTICAL = 5
private const val TAG_VIEW_MINIMUM_HEIGHT_DP = 32f
private const val TAG_VIEW_MINIMUM_WIDTH_DP = 100f

class TabooTagView(context: Context, attrs: AttributeSet): AppCompatTextView(context, attrs) {
    private val backgroundColor: Any? = null

    init {
        val typed = context.obtainStyledAttributes(attrs, R.styleable.TabooTagView)
        val tagBackgroundColor = typed.getColorStateList(R.styleable.TabooTagView_tagBackgroundColor)
        val textColor = typed.getColor(R.styleable.TabooTagView_android_textColor, ContextCompat.getColor(context, R.color.white))

        typed.recycle()

        setPadding(TAG_VIEW_PADDING_HORIZONTAL, TAG_VIEW_PADDING_VERTICAL, TAG_VIEW_PADDING_HORIZONTAL,TAG_VIEW_PADDING_VERTICAL)
        setBackgroundDrawable(ContextCompat.getDrawable(context,R.drawable.shape_rect_r10_a100_000000))
        setTagBackgroundColor(tagBackgroundColor)
        setTextColor(textColor)
        textAlignment = TEXT_ALIGNMENT_CENTER
        gravity = Gravity.CENTER

        val density = context.resources.displayMetrics.density
        minimumHeight = Math.round(TAG_VIEW_MINIMUM_HEIGHT_DP * density)
        minimumWidth = Math.round(TAG_VIEW_MINIMUM_WIDTH_DP * density)
    }

    fun setTagBackgroundColor(color: Any?) {
        val background = background as? GradientDrawable
        if (background != null) {
            when (color) {
                is Int -> background.setColor(color)            // 단일 색상 처리
                is ColorStateList -> background.color = color   // ColorStateList 처리
                else -> background.setColor(ContextCompat.getColor(context, R.color.taboo_blue_700))              // 기본값
            }
        }
    }
}