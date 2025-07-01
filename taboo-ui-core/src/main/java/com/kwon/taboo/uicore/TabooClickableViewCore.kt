package com.kwon.taboo.uicore

import android.Manifest
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_CANCEL
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_UP
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.core.content.getSystemService
import androidx.core.content.withStyledAttributes
import com.kwon.taboo.uicore.animation.ScaleXYAnimation
import com.kwon.taboo.uicore.attribute.ButtonAnimation

open class TabooClickableViewCore @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
): FrameLayout(context, attrs, defStyle) {
    private val vibrator = if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) context.getSystemService<Vibrator>()
    else context.getSystemService(Vibrator::class.java)
    private var vibrationDuration: Long = 20L // 진동 시간

    /**
     * 클릭 시 Vibration 효과 여부
     */
    private var enabledVibration: Boolean = true

    /**
     * [ACTION_DOWN], [ACTION_UP], [ACTION_CANCEL] 이벤트 발생 시 동작하는 뷰 스케일 변경 애니메이션 동작 여부
     */
    private var enabledScaleAnimation = true

    /**
     * [ACTION_DOWN] 이벤트 발생 시 뷰의 크기를 변경하는 애니메이션.
     */
    private var enterScaleAnimation = ScaleXYAnimation(this)

    /**
     * [ACTION_UP], [ACTION_UP] 이벤트 발생 시 뷰의 크기를 변경하는 애니메이션.
     */
    private var exitScaleAnimation = ScaleXYAnimation(this)

    private var enterAnimationListener: ScaleXYAnimation.ScaleAnimatorListener? = null

    private var exitAnimationListener: ScaleXYAnimation.ScaleAnimatorListener? = null

    init {
        isClickable = true
        isFocusable = true

        context.withStyledAttributes(attrs, R.styleable.TabooClickableViewCore) {
            setEnabledScaleAnimation(getBoolean(R.styleable.TabooClickableViewCore_enabledAnimation, true))
            setScaleAnimationDuration(getInt(R.styleable.TabooClickableViewCore_animationDuration, 100).toLong())
            setScaleAnimationRatio(getFloat(R.styleable.TabooClickableViewCore_scaleAnimationRatio, 0.95f))
            setEnabledVibration(getBoolean(R.styleable.TabooClickableViewCore_enabledVibration, true))
            setVibrationDuration(getInt(R.styleable.TabooClickableViewCore_vibrationDuration, 20).toLong())
        }

        enterScaleAnimation.addListener(enterAnimationListener)

        exitScaleAnimation.addListener(exitAnimationListener)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (enabledScaleAnimation && isEnabled) {
            when (ev?.action) {
                ACTION_DOWN -> enterScaleAnimation.start()

                ACTION_CANCEL,
                ACTION_UP -> exitScaleAnimation.start()
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

    fun setEnterScaleAnimationListener(scaleAnimatorListener: ScaleXYAnimation.ScaleAnimatorListener?) {
        enterScaleAnimation.addListener(scaleAnimatorListener)
    }

    fun getEnterScaleAnimationListener() = this.enterAnimationListener

    fun setExitScaleAnimationListener(scaleAnimatorListener: ScaleXYAnimation.ScaleAnimatorListener?) {
        exitScaleAnimation.addListener(scaleAnimatorListener)
    }

    fun getExitScaleAnimationListener() = this.exitAnimationListener

    fun setEnabledScaleAnimation(enabled: Boolean) {
        enabledScaleAnimation = enabled
    }

    fun isEnabledScaleAnimation(): Boolean {
        return enabledScaleAnimation
    }

    /**
     * [ACTION_DOWN] 이벤트 시 버튼의 크기를 조정하는 비율을 설정합니다.
     *
     * [ButtonAnimation]의 `endValue`는 [ACTION_DOWN] 이벤트 시 버튼이 눌렸을 때의 크기 비율을 나타내기 때문에
     * EndValue를 설정합니다.
     * @param scaleRatio 버튼이 눌렸을 때의 크기 비율 (예: 0.95f는 95% 크기로 축소)
     */
    fun setScaleAnimationRatio(scaleRatio: Float) {
        enterScaleAnimation.setScaleXY(1f, scaleRatio)
        exitScaleAnimation.setScaleXY(scaleRatio, 1f)
    }

    fun getScaleAnimationRatio(): FloatArray {
        return enterScaleAnimation.getScaleXY()
    }

    fun setScaleAnimationDuration(duration: Long) {
        enterScaleAnimation.setDuration(duration)
        exitScaleAnimation.setDuration(duration)
    }

    fun getScaleAnimationDuration(): Long {
        return enterScaleAnimation.getDuration()
    }

    fun setEnabledVibration(enabled: Boolean) {
        this.enabledVibration = enabled
    }

    fun getEnabledVibration(): Boolean {
        return enabledVibration
    }

    fun setVibrationDuration(duration: Long) {
        this.vibrationDuration = duration
    }

    fun getVibrationDuration(): Long {
        return vibrationDuration
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
        val vibrationEffect = VibrationEffect.createOneShot(vibrationDuration, 1)
        vibrator?.vibrate(vibrationEffect)
    }

    /**
     * 안드로이드 8.0 미만 버전에서 진동
     */
    @RequiresPermission(Manifest.permission.VIBRATE)
    private fun startVibrationLegacy() {
        vibrator?.vibrate(vibrationDuration)
    }
}