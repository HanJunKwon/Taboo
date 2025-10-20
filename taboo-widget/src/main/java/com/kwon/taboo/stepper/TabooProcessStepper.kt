package com.kwon.taboo.stepper

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.LinearLayout
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
            // Stepper 개수
            setStepCount(getInt(R.styleable.TabooProcessStepper_stepCount, 0))
        }
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

        // StepperItem 생성
        repeat(stepCount) { position ->
            if (position != stepCount) {
                stepperItems.add(TabooProcessStepperItem(context, stepSpacing))
            } else {
                stepperItems.add(TabooProcessStepperItem(context))
            }
        }

        // Stepper 아이템 추가
        stepperItems.forEach { addView(it) }

        // 첫 번째 Stepper 아이템 활성화
        stepperItems[0].setIndicate(true)

        this.stepCount = stepCount
    }

    /**
     * step의 개수를 반환합니다.
     */
    fun getStepCount() : Int {
        return this.stepCount
    }

    /**
     * 현재 단계를 바꿉니다.
     *
     */
    private fun setStepPositionInternal(stepPosition: Int) {
        if (stepPosition < MIN_STEP_COUNT - 1 || stepPosition >= stepCount) {
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

    fun getStepPosition(): Int {
        return stepPosition
    }

    fun next() {
        setStepPosition(getStepPosition() + 1)
    }

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