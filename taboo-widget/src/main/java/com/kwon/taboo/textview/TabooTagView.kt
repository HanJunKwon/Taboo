package com.kwon.taboo.textview

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.kwon.taboo.R
import com.hansae.taboo.core.util.ResourceUtils

private const val TAG_VIEW_PADDING_HORIZONTAL = 15f
private const val TAG_VIEW_PADDING_VERTICAL = 5f
private const val TAG_VIEW_MINIMUM_HEIGHT_DP = 32f

class TabooTagView(context: Context, attrs: AttributeSet): AppCompatTextView(context, attrs) {
    init {
        val typed = context.obtainStyledAttributes(attrs, R.styleable.TabooTagView)
        val tagBackgroundColor = typed.getColorStateList(R.styleable.TabooTagView_tagBackgroundColor)
        val textColor = typed.getColor(R.styleable.TabooTagView_android_textColor, ContextCompat.getColor(context, com.kwon.taboo.uicore.R.color.white))

        typed.recycle()

        val verticalPadding = ResourceUtils.dpToPx(context, TAG_VIEW_PADDING_VERTICAL)
        val horizontalPadding = ResourceUtils.dpToPx(context, TAG_VIEW_PADDING_HORIZONTAL)
        setPadding(
            horizontalPadding,
            verticalPadding,
            horizontalPadding,
            verticalPadding
        )
        setBackgroundDrawable(ContextCompat.getDrawable(context,R.drawable.shape_rect_r10_a100_000000))
        setTagBackgroundColor(tagBackgroundColor)
        setTextColor(textColor)
        textAlignment = TEXT_ALIGNMENT_CENTER
        gravity = Gravity.CENTER

        val density = context.resources.displayMetrics.density
        minimumHeight = Math.round(TAG_VIEW_MINIMUM_HEIGHT_DP * density)
    }

    fun setTagBackgroundColor(color: Any?) {
        val background = background as? GradientDrawable
        if (background != null) {
            when (color) {
                is Int -> background.setColor(color)            // 단일 색상 처리
                is ColorStateList -> background.color = color   // ColorStateList 처리
                else -> background.setColor(ContextCompat.getColor(context, com.kwon.taboo.uicore.R.color.taboo_blue_700))              // 기본값
            }
        }
    }
}