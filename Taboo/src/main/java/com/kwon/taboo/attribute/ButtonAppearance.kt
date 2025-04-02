package com.kwon.taboo.attribute

import android.content.Context
import androidx.annotation.ColorInt
import androidx.annotation.IntDef
import androidx.annotation.RestrictTo
import androidx.core.content.ContextCompat
import com.kwon.taboo.R
import com.kwon.taboo.attribute.ButtonAppearance.Companion.BUTTON_SHAPE_RECT

@RestrictTo(RestrictTo.Scope.LIBRARY)
class ButtonAppearance(private val context: Context) {
    /**
     * 버튼의 모양을 정의한다.
     *
     * - [BUTTON_SHAPE_RECT]: 직사각형 모양의 버튼
     * - [BUTTON_SHAPE_ROUNDED]: 둥근 모서리를 가진 버튼
     */
    private var buttonShape: Int = BUTTON_SHAPE_RECT

    /**
     * 버튼의 모양을 제외한 내부의 전반적인 디자인을 관리합니다.
     */
    private var buttonType: Int = BUTTON_TYPE_SOLID

    /**
     * [buttonShape]가 [BUTTON_SHAPE_ROUNDED]일 때 사용되는 버튼의 둥근 모서리의 반지름입니다.
     */
    private var radius: Float = 0f

    /**
     * 버튼의 대표 색상과 보조 색상을 관리하는 객체입니다.
     * 각 색상을 사용하는 방식은 버튼의 특성에 따라 다릅니다.
     */
    private lateinit var colorContainer: ColorContainer

    /**
     * 버튼 클릭 시 발생하는 Ripple 애니메이션 효과 색상을 지정합니다.
     *
     * 기본값은 [R.color.taboo_button_ripple_color]으로 색상은 `#000000`이며 투명도는 `66%`입니다.
     */
    @ColorInt
    private var rippleColor: Int = ContextCompat.getColor(context, R.color.taboo_button_ripple_color)

    constructor(
        context: Context,
        buttonShape: Int,
        buttonType: Int,
        colorContainer: ColorContainer,
        rippleColor: Int
    ) : this(context) {
        setButtonShape(buttonShape)
        setButtonType(buttonType)
        setColorContainer(colorContainer)
        setRippleColor(rippleColor)
    }

    fun setButtonShape(@ButtonShape buttonShape: Int) {
        this.buttonShape = buttonShape

        val radius = when (buttonShape) {
            BUTTON_SHAPE_ROUNDED -> context.resources.getDimension(R.dimen.taboo_button_round_shape_radius)
            else -> 0f
        }
        setRadius(radius)
    }

    @ButtonShape
    fun getButtonShape(): Int {
        return buttonShape
    }

    fun setButtonType(@ButtonType buttonType: Int) {
        this.buttonType = buttonType
    }

    @ButtonType
    fun getButtonType(): Int {
        return buttonType
    }

    fun setColorContainer(colorContainer: ColorContainer) {
        this.colorContainer = colorContainer
    }

    fun getColorContainer(): ColorContainer {
        return colorContainer
    }

    fun setPrimaryColor(@ColorInt primaryColor: Int) {
        colorContainer.primaryColor = primaryColor
    }

    @ColorInt
    fun getPrimaryColor(): Int {
        return colorContainer.primaryColor
    }

    fun setSecondaryColor(@ColorInt secondaryColor: Int) {
        colorContainer.secondaryColor = secondaryColor
    }

    @ColorInt
    fun getSecondaryColor(): Int {
        return colorContainer.secondaryColor
    }

    fun setRippleColor(@ColorInt rippleColor: Int) {
        this.rippleColor = rippleColor
    }

    @ColorInt
    fun getRippleColor(): Int {
        return rippleColor
    }

    fun setRadius(radius: Float) {
        this.radius = radius
    }

    fun getRadius(): Float {
        return radius
    }

    @IntDef(BUTTON_SHAPE_RECT, BUTTON_SHAPE_ROUNDED)
    @Retention(AnnotationRetention.SOURCE)
    annotation class ButtonShape

    @IntDef(BUTTON_TYPE_SOLID, BUTTON_TYPE_FILL, BUTTON_TYPE_OUTLINE, BUTTON_TYPE_DASH)
    @Retention(AnnotationRetention.SOURCE)
    annotation class ButtonType

    companion object {
        const val BUTTON_SHAPE_RECT = 0
        const val BUTTON_SHAPE_ROUNDED = 1

        const val BUTTON_TYPE_SOLID = 0
        const val BUTTON_TYPE_FILL = 1
        const val BUTTON_TYPE_OUTLINE = 2
        const val BUTTON_TYPE_DASH = 3
    }
}