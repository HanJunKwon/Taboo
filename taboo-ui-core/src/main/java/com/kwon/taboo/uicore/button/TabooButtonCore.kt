package com.kwon.taboo.uicore.button

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.StyleRes
import androidx.core.content.ContextCompat
import com.kwon.taboo.uicore.TabooClickableViewCore
import com.kwon.taboo.uicore.attribute.ButtonAppearance
import com.kwon.taboo.uicore.attribute.ButtonAppearance.Companion.BUTTON_TYPE_DASH
import com.kwon.taboo.uicore.attribute.ButtonAppearance.Companion.BUTTON_TYPE_FILL
import com.kwon.taboo.uicore.attribute.ButtonAppearance.Companion.BUTTON_TYPE_OUTLINE
import com.kwon.taboo.uicore.attribute.ButtonAppearance.Companion.BUTTON_TYPE_SOLID
import com.kwon.taboo.uicore.attribute.ColorContainer
import com.kwon.taboo.uicore.attribute.Stroke

abstract class TabooButtonCore(context: Context, attrs: AttributeSet): TabooClickableViewCore(context, attrs) {
    protected val gradientDrawable = GradientDrawable()

    /**
     * 버튼의 스타일을 정의하는 객체.
     */
    private var buttonAppearance = ButtonAppearance(context)

    /**
     * 버튼의 텍스트.
     */
    private var text: String? = null

    /**
     * 버튼의 텍스트 모양을 정의하는 스타일.
     */
    @StyleRes
    protected val textAppearance: Int = 0

    fun setButtonAppearance(buttonAppearance: ButtonAppearance) {
        this.buttonAppearance = buttonAppearance
    }

    fun getButtonAppearance(): ButtonAppearance {
        return buttonAppearance
    }

    /**

     */
    fun setButtonShape(@ButtonAppearance.ButtonShape buttonShape: Int) {
        buttonAppearance.setButtonShape(buttonShape)

        drawButton()
    }

    fun setButtonType(@ButtonAppearance.ButtonType buttonType: Int) {
        buttonAppearance.setButtonType(buttonType)

        drawButton()
    }

    /**
     * 버튼의 대표 색상을 지정 합니다.
     *
     * 만약 [colorContainer]가 `null`이라면 새로운 [ColorContainer]를 생성하여 대표 색상을 지정합니다.
     * @param primaryColor 대표 색상
     */
    open fun setPrimaryColor(@ColorInt primaryColor: Int) {
        buttonAppearance.setPrimaryColor(primaryColor)

        drawButton()
    }

    /**
     * 버튼의 대표 색상을 지정 합니다.
     * @param primaryColorResId 대표 색상의 리소스 아이디 (e.g R.color.primaryColor)
     */
    fun setPrimaryColorResId(@ColorRes primaryColorResId: Int) {
        setPrimaryColor(ContextCompat.getColor(context, primaryColorResId))

        drawButton()
    }

    /**
     * 버튼의 대표 색상을 반환 합니다.
     * @return 버튼의 대표 색상. 만약 [colorContainer]가 `null`이라면 `IllegalStateException`을 발생시킵니다.
     */
    @ColorInt
    fun getPrimaryColor(): Int {
        return buttonAppearance.getPrimaryColor()
    }

    /**
     * 버튼의 보조 색상을 지정 합니다.
     *
     * 만약 [colorContainer]가 `null`이라면 새로운 [ColorContainer]를 생성하여 대표 색상을 지정합니다.
     * @param secondaryColor 보조 색상
     */
    fun setSecondaryColor(@ColorInt secondaryColor: Int) {
        buttonAppearance.setSecondaryColor(secondaryColor)

        drawButton()
    }

    /**
     * 버튼의 보조 색상을 지정 합니다.
     * @param secondaryColorResId 보조 색상의 리소스 아이디 (e.g R.color.secondaryColor)
     */
    fun setSecondaryColorResId(@ColorRes secondaryColorResId: Int) {
        setSecondaryColor(ContextCompat.getColor(context, secondaryColorResId))

        drawButton()
    }

    /**
     * 버튼의 보조 색상을 반환합니다.
     *
     * @return 버튼의 보조 색상. 만약 [colorContainer]가 `null`이라면 `IllegalStateException`을 발생시킵니다.
     */
    @ColorInt
    fun getSecondaryColor(): Int {
        return buttonAppearance.getSecondaryColor()
    }

    /**
     * 버튼의 대표 색상과 보조 색상을 관리하는 [ColorContainer]를 지정 합니다.
     */
    fun setColorContainer(colorContainer: ColorContainer) {
        buttonAppearance.setColorContainer(colorContainer)

        drawButton()
    }

    /**
     * 버튼의 대표 색상과 보조 색상을 관리하는 [ColorContainer]를 반환 합니다.
     */
    fun getColorContainer(): ColorContainer {
        return buttonAppearance.getColorContainer()
    }

    /**
     * 버튼 클릭 시 발생하는 Ripple 애니메이션 효과 색상을 지정합니다.
     * @param rippleColor Ripple 애니메이션 효과 색상
     */
    fun setRippleColor(@ColorInt rippleColor: Int) {
        buttonAppearance.setRippleColor(rippleColor)

        drawButton()
    }

    /**
     * 버튼 클릭 시 발생하는 Ripple 애니메이션 효과 색상을 지정합니다.
     * @param rippleColorResId Ripple 애니메이션 효과 색상의 리소스 아이디 (e.g R.color.rippleColor)
     */
    fun setRippleColorResId(@ColorRes rippleColorResId: Int) {
        setRippleColor(ContextCompat.getColor(context, rippleColorResId))

        drawButton()
    }

    /**
     * 버튼 클릭 시 발생하는 Ripple 애니메이션 효과 색상을 반환합니다.
     * @return Ripple 애니메이션 효과 색상
     */
    @ColorInt
    fun getRippleColor(): Int {
        return buttonAppearance.getRippleColor()
    }

    /**
     * 버튼의 모서리 둥글기를 지정합니다.
     */
    fun setRadius(radius: Float) {
        buttonAppearance.setRadius(radius)
    }

    /**
     * 버튼의 모서리 둥글기를 반환합니다.
     */
    fun getRadius(): Float {
        return buttonAppearance.getRadius()
    }

    fun getBackgroundColor(): ColorStateList {
        val buttonType = buttonAppearance.getButtonType()
        val colorContainer = buttonAppearance.getColorContainer()
        return when (buttonType) {
            BUTTON_TYPE_SOLID -> colorContainer.getPrimaryColorStateList()
            BUTTON_TYPE_FILL -> colorContainer.getSecondaryColorStateList()
            BUTTON_TYPE_OUTLINE -> ColorStateList.valueOf(ContextCompat.getColor(context, android.R.color.transparent))
            BUTTON_TYPE_DASH -> ColorStateList.valueOf(ContextCompat.getColor(context, android.R.color.transparent))
            else -> throw IllegalArgumentException("Unknown button type")
        }
    }

    /**
     * 버튼의 [Stroke]를 반환합니다.
     */
    fun getButtonStroke(): Stroke {
        val buttonType = buttonAppearance.getButtonType()
        val colorContainer = buttonAppearance.getColorContainer()

        return Stroke(
            width = if (buttonType == BUTTON_TYPE_DASH || buttonType == BUTTON_TYPE_OUTLINE) 1 else 0,
            color = colorContainer.getPrimaryColorStateList(),
            dashWidth = if (buttonType == BUTTON_TYPE_DASH) 5f else 0f,
            dashGap = if (buttonType == BUTTON_TYPE_DASH) 5f else 0f
        )
    }

    fun GradientDrawable.setStroke(stroke: Stroke) {
        setStroke(stroke.width, stroke.color, stroke.dashWidth, stroke.dashGap)
    }

    fun getRippleColorState(): ColorStateList {
        return ColorStateList(
            arrayOf(
                intArrayOf(android.R.attr.state_pressed),
                intArrayOf(android.R.attr.state_hovered),
                intArrayOf()
            ),
            intArrayOf(
                buttonAppearance.getRippleColor(),
                buttonAppearance.getRippleColor(),
                android.R.color.transparent
            )
        )
    }

    open fun setText(text: String) {
        this.text = text

        updateText()
    }

    abstract fun updateText()

    fun getText(): String? {
        return text
    }

    /**
     * 버튼의 배경 리소스를 생성하는 추상 메서드입니다.
     * 버튼별로 다른 배경을 생성하기 때문에 하위 클래스에서 구현해야 합니다.
     *
     * @return 버튼의 배경 리소스
     */
    abstract fun createBackground(): RippleDrawable

    abstract fun drawButton()
}