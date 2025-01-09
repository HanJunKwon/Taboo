package com.kwon.taboo.numbering

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity.CENTER
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.kwon.taboo.R

class TabooNumberingBall(
    context: Context,
    attrs: AttributeSet? = null
): AppCompatTextView(context, attrs) {

    init {
        val typed = context.obtainStyledAttributes(attrs, R.styleable.TabooNumberingBall)

        typed.recycle()

        initNumberingBall()
    }

    private fun initNumberingBall() {
        setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.shape_numbering_ball_default))
        setTextAppearance(R.style.Taboo_TextAppearance_NumberingBall)
        gravity = CENTER
        textAlignment = TEXT_ALIGNMENT_CENTER
        isActivated = false
    }
}