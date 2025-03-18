package com.kwon.taboo.timer

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.IntDef
import androidx.constraintlayout.widget.ConstraintLayout
import com.kwon.taboo.R
import com.kwon.taboo.databinding.TabooTimerBinding

class TabooTimer(
    context: Context,
    attrs: AttributeSet
): ConstraintLayout(context, attrs) {
    private val binding = TabooTimerBinding.inflate(LayoutInflater.from(context), this, true)

    private var state = STATE_STOP

    private var settingTime = 0L
    private var remainTime = 0L

    init {
        val typed = context.obtainStyledAttributes(attrs, R.styleable.TabooTimer)

        typed.recycle()
    }

    /**
     * 시간을 설정합니다.
     */
    fun setTime(millis: Long) {

    }

    /**
     * 남은 시간을 설정합니다.
     */
    fun setRemainTime(millis: Long) {

    }

    /**
     * 타이머를 시작합니다.
     *
     * 이미 시작된 상태라면 아무런 동작을 하지 않습니다.
     */
    fun start() {

    }

    /**
     * 타이머를 일시정지합니다.
     *
     * 이미 일시정지 상태라면 아무런 동작을 하지 않습니다.
     */
    fun pause() {

    }

    /**
     * 타이머를 완전히 중지합니다.
     *
     * 이미 중지된 상태이기 때문에 [start]를 호출하여도 타이머가 시작하지 않습니다.
     * 타이머를 다시 시작하고 싶으면 [setTime]으로 시간을 다시 설정한 후에 [start]를 호출해야 합니다.
     */
    fun stop() {

    }

    @IntDef(STATE_START, STATE_PAUSE, STATE_STOP)
    @Retention(AnnotationRetention.SOURCE)
    annotation class TimerState

    companion object {
        const val STATE_START = 0
        const val STATE_PAUSE = 1
        const val STATE_STOP = 2
    }

}