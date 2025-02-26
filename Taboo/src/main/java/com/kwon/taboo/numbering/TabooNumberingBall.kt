package com.kwon.taboo.numbering

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.Gravity.CENTER
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.kwon.taboo.R

class TabooNumberingBall(
    context: Context,
    attrs: AttributeSet? = null
): AppCompatTextView(context, attrs) {

    /**
     * Ball 최소 너비
     */
    private var ballMinWidth = R.dimen.taboo_numbering_ball_min_size

    /**
     * Ball 최소 높이
     */
    private var ballMinHeight = R.dimen.taboo_numbering_ball_min_size

    /**
     * Ball 색상
     */
    private var ballColor: ColorStateList? = null

    init {
        val typed = context.obtainStyledAttributes(attrs, R.styleable.TabooNumberingBall)
        val ballColor = typed.getColorStateList(R.styleable.TabooNumberingBall_ballColor)

        typed.recycle()

        setBallColorInternal(ballColor)

        initNumberingBall()
    }

    /**
     * Ball 모양 업데이트
     */
    private fun updateBallShape() {
        setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.shape_numbering_ball_default))
    }

    /**
     * Ball 크기 업데이트.
     * - 최소 너비와 높이
     */
    private fun updateBallSize() {
        val minWidth = resources.getDimensionPixelSize(ballMinWidth)
        val minHeight = resources.getDimensionPixelSize(ballMinHeight)
        setMinWidth(minWidth)
        setMinHeight(minHeight)
    }

    /**
     * 내부적으로 공(ball)의 색상을 설정하는 메서드.
     *
     * 전달된 `ColorStateList`가 `null`이면 기본 색상 [R.color.selector_taboo_numbering_ball_solid]을 사용한다.
     *
     * @param color Ball 색상.
     */
    private fun setBallColorInternal(color: ColorStateList?) {
        ballColor = color ?: ContextCompat.getColorStateList(context, R.color.selector_taboo_numbering_ball_solid)
    }

    /**
     * Ball 색상을 단일 `Color` 값으로 설정하는 메서드.
     *
     * `Color` 값을 [ColorStateList]로 변환하여 설정한다.
     *
     * [ColorStateList]로 설정하려면 `setBallColor(color: ColorStateList?)` 메서드를 사용한다.
     *
     * @param color Ball 단일 색상.
     */
    fun setBallColor(color: Int) {
        setBallColor(ContextCompat.getColorStateList(context, color))
    }

    /**
     * Ball 색상을 [ColorStateList]로 설정하는 메서드.
     *
     * 내부적으로 색상을 설정한 후, UI 업데이트를 수행한다.
     *
     * @param color Ball 색상.
     */
    fun setBallColor(color: ColorStateList?) {
        setBallColorInternal(color)

        updateBallColor()
    }

    /**
     * Ball 색상을 업데이트하는 메서드.
     *
     * [getBackground]를 [GradientDrawable]로 캐스팅하여 색상을 변경한다.
     * [GradientDrawable]로 캐스팅할 수 없으면 아무 작업도 수행하지 않는다.
     */
    private fun updateBallColor() {
        val gradientDrawable = background as? GradientDrawable
        gradientDrawable?.color = ballColor
    }

    private fun initNumberingBall() {
        updateBallShape()
        updateBallSize()
        updateBallColor()
        setTextAppearance(R.style.Taboo_TextAppearance_NumberingBall)
        gravity = CENTER
        textAlignment = TEXT_ALIGNMENT_CENTER
        isSelected = false
    }
}