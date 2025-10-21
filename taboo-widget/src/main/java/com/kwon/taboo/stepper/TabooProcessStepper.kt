package com.kwon.taboo.stepper

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes
import com.kwon.taboo.R
import com.kwon.taboo.uicore.util.ResourceUtils

class TabooProcessStepper @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
): LinearLayout(context, attrs, defStyle) {
    private var stepCount = 0
    private var stepSpacing = ResourceUtils.dpToPx(context, 10f)

    private var stepPosition = 0
    private var stepperItems = mutableListOf<TabooProcessStepperItem>()

    init {
        context.withStyledAttributes(attrs, R.styleable.TabooProcessStepper) {
            // Step 개수
            setStepCount(getInt(R.styleable.TabooProcessStepper_stepCount, 0))

            // Step 간격
            setStepSpacing(
                spacing = getDimensionPixelSize(
                    R.styleable.TabooProcessStepper_stepSpacing,
                    ResourceUtils.dpToPx(context, 10f)
                )
            )

            // Step Track 색상
            setStepTrackColor(
                color = getColor(
                    R.styleable.TabooProcessStepper_trackColor,
                    ContextCompat.getColor(context, com.kwon.taboo.uicore.R.color.taboo_black_800)
                )
            )

            // Step Indicator 색상
            setIndicatorColor(
                color = getColor(
                    R.styleable.TabooProcessStepper_indicatorColor,
                    ContextCompat.getColor(context, com.kwon.taboo.uicore.R.color.taboo_blue_600)
                )
            )
        }
    }

    private fun drawStepperItems() {
        // StepperItem 생성
        repeat(stepCount) { position ->
            val item = TabooProcessStepperItem(context).apply {
                if (position <= stepPosition) {
                    setIndicate(true)
                }
            }

            stepperItems.add(item)
        }

        // Stepper 아이템 추가
        stepperItems.forEach { addView(it) }
    }

    /**
     * step의 개수를 지정합니다.
     */
    private fun setStepCount(stepCount: Int) {
        require(stepCount >= MIN_STEP_COUNT) {
            "Step count must be at least 1."
        }

        require (stepCount <= MAX_STEP_COUNT) {
            "Step count must be 10 or fewer."
        }

        this.stepCount = stepCount

        drawStepperItems()
    }

    /**
     * step의 개수를 반환합니다.
     */
    fun getStepCount() : Int {
        return this.stepCount
    }

    /**
     * Step 간격을 설정합니다.
     * @param spacing Step의 간격 (단위: px)
     */
    fun setStepSpacing(spacing: Int) {
        this.stepSpacing = spacing

        updateStepSpacing()
    }

    private fun updateStepSpacing() {
        stepperItems.dropLast(1).forEach {
            it.setSpacing(spacing = stepSpacing)
        }
    }

    /**
     * Step 간격을 반환합니다.
     * @return step 간격  (단위: px)
     */
    fun getStepSpacing() : Int {
        return stepSpacing
    }

    fun setStepTrackColor(@ColorInt color: Int) {
        if (color != 0) {
            stepperItems.forEach {
                it.setTrackColor(color)
            }
        }
    }

    fun setIndicatorColor(@ColorInt color: Int) {
        if (color != 0) {
            stepperItems.forEach {
                it.setIndicatorColor(color)
            }
        }
    }

    /**
     * 현재 단계를 바꿉니다.
     */
    private fun setStepPositionInternal(stepPosition: Int) {
        if (stepPosition + 1 < MIN_STEP_COUNT || stepPosition >= stepCount) {
            Log.w("TabooProcessStepper", "Cannot move to this position. position: $stepPosition")
            return
        }

        val prevStepPosition = this.stepPosition
        this.stepPosition = stepPosition

        when {
            prevStepPosition > stepPosition -> {
                for (i in stepPosition + 1..prevStepPosition) {
                    stepperItems[i].setIndicate(false)
                }
            }
            prevStepPosition < stepPosition -> {
                for (i in prevStepPosition..stepPosition) {
                    stepperItems[i].setIndicate(true)
                }
            }
            else -> {}
        }
    }

    fun setStepPosition(stepPosition: Int) {
        setStepPositionInternal(stepPosition)
    }

    /**
     * 현재 활성화된 포지션의 Index를 반환합니다.
     */
    fun getStepPosition(): Int {
        return stepPosition
    }

    /**
     * 다음 Stepper를 활성화합니다.
     */
    fun next() {
        setStepPosition(getStepPosition() + 1)
    }

    /**
     * 현재 Stepper를 비활성화합니다.
     */
    fun prev() {
        setStepPosition(getStepPosition() - 1)
    }
}

/**
 *
 */
private const val MIN_STEP_COUNT = 1

/**
 * Stepper를 추가할 수 있는 최대 개수.
 */
private const val MAX_STEP_COUNT = 10