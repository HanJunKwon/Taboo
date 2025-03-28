package com.kwon.taboo.timer

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import androidx.annotation.IntDef
import androidx.constraintlayout.widget.ConstraintLayout
import com.kwon.taboo.R
import com.kwon.taboo.databinding.TabooTimerBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class TabooTimer(
    context: Context,
    attrs: AttributeSet
): ConstraintLayout(context, attrs) {
    private val binding = TabooTimerBinding.inflate(LayoutInflater.from(context), this, true)

    private var state: MutableStateFlow<Int> = MutableStateFlow(STATE_STOP)

    private var settingTimeMillis = 0L
    private var remainTimeMillis = 0L

    private var timerJob: Job? = null

    init {
        val typed = context.obtainStyledAttributes(attrs, R.styleable.TabooTimer)

        typed.recycle()

        setEvent()

        setObserve()
    }

    private fun setEvent() {
        binding.circleProgressTimer.setOnClickListener {
            when (state.value) {
                STATE_START -> pause()
                STATE_PAUSE -> start()
                STATE_STOP -> start()
            }
        }
    }

    private fun setObserve() {
        CoroutineScope(Dispatchers.Default).launch {
            state.collectLatest { state ->
                setActivated(state == STATE_START)

                when (state) {
                    STATE_START -> handleTimerStart()
                    STATE_PAUSE -> handleTimerPause()
                }
            }
        }
    }

    private fun createTimerJob() {
        timerJob = CoroutineScope(Dispatchers.Default).launch {
            while (state.value == STATE_START) {
                remainTimeMillis -= 50

                val time = formatTime(remainTimeMillis)
                updateRemainTimer(time)

                // 타이머 프로그레스 업데이트
                val progress = ((remainTimeMillis.toDouble() / settingTimeMillis) * 1000).toInt()
                updateProgressTimer(progress)

                // 남은 시간이 없으면 타이머 종료
                if (remainTimeMillis <= 0) {
                    setTimerState(STATE_STOP)
                    break
                }

                kotlinx.coroutines.delay(50)
            }
        }
    }

    override fun setActivated(isActivated: Boolean) {
        binding.tvRemainTime.isActivated = isActivated
        binding.tvSettingTime.isActivated = isActivated
        binding.ivBundleTimerStatus.isActivated = isActivated
    }

    private fun setTimerState(@TimerState state: Int) {
        this.state.value = state
    }

    /**
     * 시간을 설정합니다.
     *
     * 단, 타이머가 동작 중일 때는 시간을 설정 할 수 없습니다.
     * [stop]을 호출하여 타이머를 중지한 후에 시간을 설정해야 합니다.
     */
    fun setTime(millis: Long) {
        if (state.value != STATE_STOP) {
            Log.e(">>>", "Do not set time while timer is running.")
            return
        }

        settingTimeMillis = millis

        updateSettingTime()
    }

    private fun updateSettingTime() {
        binding.tvRemainTime.text = formatTime(settingTimeMillis)
        binding.tvSettingTime.text = formatSettingTime()

        val progress = ((remainTimeMillis.toDouble() / settingTimeMillis) * 1000).toInt()
        updateProgressTimer(progress)
    }

    /**
     * 남은 시간을 설정합니다.
     *
     * 단, 설정된 시간보다 큰 시간을 설정할 수 없습니다.
     */
    fun setRemainTime(millis: Long) {
        if (settingTimeMillis < millis) {
            Log.e(">>>", "Remain time is greater than setting time.")
            return
        }

        remainTimeMillis = millis

        val progress = ((remainTimeMillis.toDouble() / settingTimeMillis) * 1000).toInt()
        updateProgressTimer(progress)
    }

    /**
     * 타이머를 시작합니다.
     *
     * 이미 시작된 상태라면 아무런 동작을 하지 않습니다.
     */
    fun start() {
        if (settingTimeMillis == 0L) {
            Log.e(">>>", "Setting time is not set.")
            return
        }

        if (remainTimeMillis == 0L) {
            Log.e(">>>", "Remain time is not set.")
            return
        }

        setTimerState(STATE_START)
    }

    private fun handleTimerStart() {
        if (timerJob == null) {
            createTimerJob()
        }

//        timerJob?.start()
    }

    private fun handleTimerPause() {
        timerJob?.cancel()
        timerJob = null
    }

    /**
     * 타이머를 일시정지합니다.
     *
     * 이미 일시정지 상태라면 아무런 동작을 하지 않습니다.
     */
    fun pause() {
        setTimerState(STATE_PAUSE)
    }

    /**
     * 타이머를 완전히 중지합니다.
     *
     * 이미 중지된 상태이기 때문에 [start]를 호출하여도 타이머가 시작하지 않습니다.
     * 타이머를 다시 시작하고 싶으면 [setTime]으로 시간을 다시 설정한 후에 [start]를 호출해야 합니다.
     */
    fun stop() {
        setTimerState(STATE_STOP)
    }

    private fun updateRemainTimer(remainTime: String) = CoroutineScope(Dispatchers.Main).launch {
        binding.tvRemainTime.text = remainTime
    }

    private fun updateProgressTimer(progress: Int) = CoroutineScope(Dispatchers.Main).launch {
        binding.circleProgressTimer.setProgressCompat(if (progress < 0) 0 else progress, true)
    }

    private fun formatSettingTime(): String{
        val minute = (settingTimeMillis / 1000) / 60
        val second = (settingTimeMillis / 1000) % 60

        return String.format("%d min, %d sec", minute, second)
    }

    private fun formatTime(millis: Long): String {
        val minutes = (millis / 1000) / 60
        val seconds = (millis / 1000) % 60

        return String.format("%02d:%02d", minutes, seconds)
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