package com.kwon.taboo.uicore.button

import android.Manifest
import android.animation.ObjectAnimator
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_CANCEL
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_UP
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.annotation.StyleRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes
import com.kwon.taboo.uicore.R
import com.kwon.taboo.uicore.attribute.ButtonAnimation
import com.kwon.taboo.uicore.attribute.ButtonAppearance
import com.kwon.taboo.uicore.attribute.ButtonAppearance.Companion.BUTTON_TYPE_DASH
import com.kwon.taboo.uicore.attribute.ButtonAppearance.Companion.BUTTON_TYPE_FILL
import com.kwon.taboo.uicore.attribute.ButtonAppearance.Companion.BUTTON_TYPE_OUTLINE
import com.kwon.taboo.uicore.attribute.ButtonAppearance.Companion.BUTTON_TYPE_SOLID
import com.kwon.taboo.uicore.attribute.ColorContainer
import com.kwon.taboo.uicore.attribute.Stroke

abstract class TabooButtonCore(context: Context, attrs: AttributeSet): ConstraintLayout(context, attrs) {
    protected val gradientDrawable = GradientDrawable()

    private val vibrator = context.getSystemService(Vibrator::class.java)

    /**
     * 버튼의 스타일을 정의하는 객체.
     */
    private var buttonAppearance = ButtonAppearance(context)

    private var enabledAnimation = true

    private var buttonAnimation = ButtonAnimation()

    private val buttonAnimationPropertyNames = listOf("scaleX", "scaleY")

    private var buttonPressedEnterObjectAnimations = mutableListOf<ObjectAnimator>()

    private var buttonPressedExitObjectAnimations = mutableListOf<ObjectAnimator>()

    /**
     * 버튼의 텍스트.
     */
    private var text: String? = null

    /**
     * 버튼의 텍스트 모양을 정의하는 스타일.
     */
    @StyleRes
    protected val textAppearance: Int = 0

    private var enabledVibration: Boolean = true

    init {
        isClickable = true
        isFocusable = true

        context.withStyledAttributes(attrs, R.styleable.TabooButtonCore) {
            setEnabledAnimation(getBoolean(R.styleable.TabooButtonCore_enabledAnimation, true))
            setAnimationDuration(getInt(R.styleable.TabooButtonCore_animationDuration, 100).toLong())
            setScaleRatio(getFloat(R.styleable.TabooButtonCore_scaleRatio, 0.95f))
            setEnabledVibration(getBoolean(R.styleable.TabooButtonCore_enabledVibration, true))

            createButtonObjectAnimators()
        }

        this.setOnClickListener(null)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (enabledAnimation) {
            when (ev?.action) {
                ACTION_DOWN -> {
                    buttonPressedEnterObjectAnimations.forEach {
                        it.start()
                    }
                }
                ACTION_CANCEL,
                ACTION_UP -> {
                    buttonPressedExitObjectAnimations.forEach {
                        it.start()
                    }
                }
            }
        }

        return super.dispatchTouchEvent(ev)
    }

    override fun setOnClickListener(l: OnClickListener?) {
        super.setOnClickListener { v ->
            l?.onClick(v)

            if (enabledVibration) {
                startVibration()
            }
        }
    }

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
    }

    fun setButtonType(@ButtonAppearance.ButtonType buttonType: Int) {
        buttonAppearance.setButtonType(buttonType)
    }

    /**
     * 버튼의 대표 색상을 지정 합니다.
     *
     * 만약 [colorContainer]가 `null`이라면 새로운 [ColorContainer]를 생성하여 대표 색상을 지정합니다.
     * @param primaryColor 대표 색상
     */
    open fun setPrimaryColor(@ColorInt primaryColor: Int) {
        buttonAppearance.setPrimaryColor(primaryColor)
    }

    /**
     * 버튼의 대표 색상을 지정 합니다.
     * @param primaryColorResId 대표 색상의 리소스 아이디 (e.g R.color.primaryColor)
     */
    fun setPrimaryColorResId(@ColorRes primaryColorResId: Int) {
        setPrimaryColor(ContextCompat.getColor(context, primaryColorResId))
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
    }

    /**
     * 버튼의 보조 색상을 지정 합니다.
     * @param secondaryColorResId 보조 색상의 리소스 아이디 (e.g R.color.secondaryColor)
     */
    fun setSecondaryColorResId(@ColorRes secondaryColorResId: Int) {
        setSecondaryColor(ContextCompat.getColor(context, secondaryColorResId))
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
    }

    /**
     * 버튼 클릭 시 발생하는 Ripple 애니메이션 효과 색상을 지정합니다.
     * @param rippleColorResId Ripple 애니메이션 효과 색상의 리소스 아이디 (e.g R.color.rippleColor)
     */
    fun setRippleColorResId(@ColorRes rippleColorResId: Int) {
        setRippleColor(ContextCompat.getColor(context, rippleColorResId))
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
            BUTTON_TYPE_OUTLINE -> context.resources.getColorStateList(android.R.color.transparent, null)
            BUTTON_TYPE_DASH -> context.resources.getColorStateList(android.R.color.transparent, null)
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

    fun setEnabledAnimation(enabled: Boolean) {
        enabledAnimation = enabled
    }

    fun isEnabledAnimation(): Boolean {
        return enabledAnimation
    }

    fun setAnimationDuration(duration: Long) {
        buttonAnimation.setDuration(duration)
    }

    fun getAnimationDuration(): Long {
        return buttonAnimation.getDuration()
    }

    /**
     * [ACTION_DOWN] 이벤트 시 버튼의 크기를 조정하는 비율을 설정합니다.
     *
     * [ButtonAnimation]의 `endValue`는 [ACTION_DOWN] 이벤트 시 버튼이 눌렸을 때의 크기 비율을 나타내기 때문에
     * EndValue를 설정합니다.
     * @param scaleRatio 버튼이 눌렸을 때의 크기 비율 (예: 0.95f는 95% 크기로 축소)
     */
    fun setScaleRatio(scaleRatio: Float) {
        buttonAnimation.setEndValue(scaleRatio)
    }

    fun getScaleRatio(): Float {
        return buttonAnimation.getEndValue()
    }

    private fun createButtonObjectAnimators() {
        buttonPressedEnterObjectAnimations.clear()
        buttonPressedExitObjectAnimations.clear()

        buttonAnimationPropertyNames.forEach { propertyName ->
            buttonPressedEnterObjectAnimations.add(
                ObjectAnimator
                    .ofFloat(this, propertyName, buttonAnimation.getStartValue(), buttonAnimation.getEndValue())
                    .apply {
                        duration = buttonAnimation.getDuration()
                        interpolator = buttonAnimation.getInterpolator()
                    }
            )

            buttonPressedExitObjectAnimations.add(
                ObjectAnimator
                    .ofFloat(this, propertyName, buttonAnimation.getEndValue(), buttonAnimation.getStartValue())
                    .apply {
                        duration = buttonAnimation.getDuration()
                        interpolator = buttonAnimation.getInterpolator()
                    }
            )
        }
    }

    open fun setText(text: String) {
        this.text = text
    }

    fun getText(): String? {
        return text
    }

    fun setEnabledVibration(enabled: Boolean) {
        this.enabledVibration = enabled
    }

    fun getEnabledVibration(): Boolean {
        return enabledVibration
    }

    @RequiresPermission(Manifest.permission.VIBRATE)
    fun startVibration() {
        when (Build.VERSION.SDK_INT) {
            in Build.VERSION_CODES.O .. Build.VERSION_CODES.Q -> {
                startVibrationO()
            }
            else -> {
                startVibrationLegacy()
            }
        }
    }

    /**
     * 안드로이드 8.0 이상 버전에서 진동
     */
    @RequiresPermission(Manifest.permission.VIBRATE)
    @RequiresApi(Build.VERSION_CODES.O)
    private fun startVibrationO() {
        val vibrationEffect = VibrationEffect.createOneShot(20L, 1)
        vibrator.vibrate(vibrationEffect)
    }

    /**
     * 안드로이드 8.0 미만 버전에서 진동
     */
    @RequiresPermission(Manifest.permission.VIBRATE)
    private fun startVibrationLegacy() {
        vibrator.vibrate(20L)
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