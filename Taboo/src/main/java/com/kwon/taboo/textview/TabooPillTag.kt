package com.kwon.taboo.textview

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.kwon.taboo.R



class TabooPillTag(
    context: Context,
    attrs: AttributeSet
): AppCompatTextView(context, attrs) {

    private val defaultPillColor = R.color.taboo_gray_03

    init {
        val typed = context.obtainStyledAttributes(attrs, R.styleable.TabooPillTag)

        initTabooTextLabel(typed)

        typed.recycle()
    }

    private fun initTabooTextLabel(typed: TypedArray) {
        setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.shape_rect_r20_a0_000000))

        val pillColor = typed.getColorStateList(R.styleable.TabooPillTag_pillColor)
        setPillColor(pillColor)
    }

    fun setPillColor(pillColor: Any?) {
        val background = background as? GradientDrawable
        when (pillColor) {
            is Int -> background?.setColor(pillColor)
            is ColorStateList -> background?.color = pillColor
            else -> background?.setColor(ContextCompat.getColor(context, defaultPillColor))
        }
    }
}