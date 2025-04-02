package com.kwon.taboo.button

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.ColorInt
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.kwon.taboo.R
import com.kwon.taboo.attribute.ButtonAppearance
import com.kwon.taboo.attribute.ColorContainer
import com.kwon.taboo.databinding.TabooBadgeButtonBinding

class TabooBadgeButton(
    context: Context,
    attrs: AttributeSet
) : ConstraintLayout(context, attrs) {

    companion object {
        const val BUTTON_SHAPE_RECT = 0
        const val BUTTON_SHAPE_ROUNDED = 1

        const val BUTTON_TYPE_SOLID = 0
        const val BUTTON_TYPE_FILL = 1
        const val BUTTON_TYPE_OUTLINE = 2
        const val BUTTON_TYPE_DASH = 3

        private const val ICON_POSITION_LEFT = 0
        private const val ICON_POSITION_RIGHT = 1

        private const val SIZE_LARGE = 0
        private const val SIZE_SMALL = 1
    }

    private val binding = TabooBadgeButtonBinding.inflate(LayoutInflater.from(context), this, true)

    private var colorContainer: ColorContainer = ColorContainer(
        primaryColor = ContextCompat.getColor(context, R.color.taboo_vibrant_blue_01),
        secondaryColor = ContextCompat.getColor(context, R.color.taboo_blue_06)
    )

    @ColorInt
    private var rippleColor: Int = ContextCompat.getColor(context, R.color.taboo_button_ripple_color)

    private var badge: Int = 0             // 뱃지 숫자
    private var text: String = ""

    init {
        val typed = context.obtainStyledAttributes(attrs, R.styleable.TabooBadgeButton)
        val text = typed.getString(R.styleable.TabooBadgeButton_android_text) ?: ""
        val primaryColor = typed.getInt(R.styleable.TabooBadgeButton_primaryColor, R.color.taboo_vibrant_blue_01)
        val rippleColor = typed.getColor(R.styleable.TabooBadgeButton_rippleColor, ContextCompat.getColor(context, R.color.taboo_button_ripple_color))
        val badge = typed.getInt(R.styleable.TabooBadgeButton_badge, 0)

        typed.recycle()

        setBadgeInternal(badge)
        setTextInternal(text)
        setPrimaryColor(primaryColor)
        setRippleColorInternal(rippleColor)

        isClickable = true

        initBadgeButton()
    }

    /**
     * [TabooBadgeButton] UI 를 초기화.
     */
    private fun initBadgeButton() {
        updateBadge()
        updateText()
        updatePrimaryColor()
    }

    fun getBadge() = badge

    /**
     * 뱃지 숫자를 설정한다.
     * @param badge 뱃지에 표시할 숫자
     */
    private fun setBadgeInternal(badge: Int) {
        this.badge = badge
    }

    /**
     * 뱃지 숫자를 설정하고, UI 를 업데이트한다.
     */
    fun setBadge(badge: Int) {
        setBadgeInternal(badge)

        updateBadge()
    }

    /**
     * 뱃지 숫자 UI 를 업데이트한다.
     */
    fun updateBadge() {
        binding.numberingBall.text = badge.toString()
    }

    private fun setTextInternal(text: String) {
        this.text = text
    }

    fun setText(text: String) {
        setTextInternal(text)

        updateText()
    }

    fun getText() = text

    private fun updateText() {
        binding.tvButtonText.text = text
    }


    // <editor-fold desc="Color">
    fun getPrimaryColor() = colorContainer.primaryColor

    fun setPrimaryColor(primaryColor: Int) {
        colorContainer.primaryColor = ContextCompat.getColor(context, primaryColor)

        updatePrimaryColor()
    }

    private fun updatePrimaryColor() {
        binding.numberingBall.setTextColor(colorContainer.primaryColor)
        binding.numberingBall.setBallColor(R.color.white)
        drawButton()
    }

    fun getRippleColor() = rippleColor

    private fun setRippleColorInternal(@ColorInt rippleColor: Int) {
        if (rippleColor != 0) {
            this.rippleColor = rippleColor
        }
    }

    /**
     * 버튼 클릭 시 발생하는 Ripple 효과의 Mask 색상을 지정한다.
     *
     * [rippleColor]가 0이면 기본 Ripple 색상인 [R.color.taboo_button_ripple_color]가 적용된다.
     */
    fun setRippleColor(@ColorInt rippleColor: Int) {
        setRippleColorInternal(rippleColor)

        drawButton()
    }
    // </editor-fold>


    private fun drawButton() {
//        val gradientDrawable = ButtonAppearance(
//            context = context,
//            buttonShape = BUTTON_SHAPE_ROUNDED,
//            buttonType = BUTTON_TYPE_SOLID,
//            colorContainer = colorContainer,
//            rippleColor = rippleColor
//        ).create()
//
//        binding.root.background = gradientDrawable
    }
}