package com.kwon.taboo.textview

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.Gravity.CENTER
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.kwon.taboo.R



class TabooPillTag(
    context: Context,
    attrs: AttributeSet
): AppCompatTextView(context, attrs) {

    private val defaultPillColor = R.color.taboo_gray_03
    private val defaultTextColor = R.color.taboo_black_02
    private val defaultTextSize = 10f

    init {
        val typed = context.obtainStyledAttributes(attrs, R.styleable.TabooPillTag)

        initTabooPillTag(typed)

        typed.recycle()
    }

    private fun initTabooPillTag(typed: TypedArray) {
        setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.shape_rect_r20_a0_000000))

        val pillColor = typed.getColorStateList(R.styleable.TabooPillTag_pillColor)
        setPillColor(pillColor)

        val fontFamily = typed.getResourceId(R.styleable.TabooPillTag_android_fontFamily, 0)
        typeface = if (fontFamily == 0) {
            ResourcesCompat.getFont(context, R.font.font_pretendard_medium)
        } else {
            ResourcesCompat.getFont(context, fontFamily)
        }

        val textColor = typed.getColorStateList(R.styleable.TabooPillTag_android_textColor)
        if (textColor == null) {
            setTextColor(ContextCompat.getColor(context, defaultTextColor))
        } else {
            setTextColor(textColor)
        }

        if (typed.hasValue(R.styleable.TabooPillTag_android_textSize)) {
            val typedValue = TypedValue()
            val isNotEmptyTextSize = typed.getValue(R.styleable.TabooPillTag_android_textSize, typedValue)
            if (isNotEmptyTextSize && typedValue.type == TypedValue.TYPE_DIMENSION) {
                setTextSize(TypedValue.COMPLEX_UNIT_PX, typed.getDimension(R.styleable.TabooPillTag_android_textSize, 0f))
            } else {
                setTextSize(TypedValue.COMPLEX_UNIT_SP, defaultTextSize)
            }
        } else {
            setTextSize(TypedValue.COMPLEX_UNIT_SP, defaultTextSize)
        }

        alignTextCenter()
    }

    fun setPillColor(pillColor: Any?) {
        val background = background as? GradientDrawable
        when (pillColor) {
            is Int -> background?.setColor(pillColor)
            is ColorStateList -> background?.color = pillColor
            else -> background?.setColor(ContextCompat.getColor(context, defaultPillColor))
        }
    }

    /**
     * 텍스트를 중앙에 맞추기
     */
    private fun alignTextCenter() {
        textAlignment = TEXT_ALIGNMENT_CENTER
        gravity = CENTER
    }
}