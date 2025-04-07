package com.kwon.taboo.counter

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.kwon.taboo.R
import com.kwon.taboo.databinding.TabooCounterBinding

class TabooCounter(context: Context, attrs: AttributeSet): ConstraintLayout(context, attrs) {
    private val binding = TabooCounterBinding.inflate(LayoutInflater.from(context), this, true)

    private var counter = 0
    private var minCount = 0
    private var maxCount = Int.MAX_VALUE

    private var minusIconTint: ColorStateList? = null
    private var plusIconTint: ColorStateList? = null

    init {
        val typed = context.obtainStyledAttributes(attrs, R.styleable.TabooCounter)
        val minCount = typed.getInt(R.styleable.TabooCounter_minCount, 0)
        val maxCount = typed.getInt(R.styleable.TabooCounter_maxCount, Int.MAX_VALUE)
        val minusIconTintList = typed.getColorStateList(R.styleable.TabooCounter_minusIconTint) ?: ContextCompat.getColorStateList(context, R.color.selector_taboo_counter_minus)
        val plusIconTintList = typed.getColorStateList(R.styleable.TabooCounter_plusIconTint) ?: ContextCompat.getColorStateList(context, R.color.selector_taboo_counter_plus)
        val enabled = typed.getBoolean(R.styleable.TabooCounter_android_enabled, true)

        typed.recycle()

        setCounter(0)
        setMinCounter(minCount)
        setMaxCounter(maxCount)
        setMinusIconTintList(minusIconTintList)
        setPlusIconTint(plusIconTintList)
        setEnable(enabled)

        setEvent()
    }

    fun setCounter(value: Int) {
        counter = value
        updateCounter()
    }

    fun getCounter(): Int {
        return counter
    }

    private fun updateCounter() {
        binding.tvCounter.text = counter.toString()
    }

    fun setMinCounter(value: Int) {
        minCount = value
    }

    fun setMaxCounter(value: Int) {
        maxCount = value
    }

    fun setMinusIconTintList(tintList: ColorStateList?) {
        this.minusIconTint = tintList
        updateMinusIconTint()
    }

    fun getMinusIconTint(): ColorStateList? {
        return minusIconTint
    }

    private fun updateMinusIconTint() {
        binding.btnMinus.imageTintList = minusIconTint
    }

    fun setPlusIconTint(tintList: ColorStateList?) {
        plusIconTint = tintList
        updatePlusIconTint()
    }

    fun getPlushIconTint(): ColorStateList? {
        return plusIconTint
    }

    private fun updatePlusIconTint() {
        binding.btnPlus.imageTintList = plusIconTint
    }

    fun setEnable(enable: Boolean) {
        binding.btnMinus.isEnabled = enable
        binding.btnPlus.isEnabled = enable
        binding.tvCounter.isEnabled = enable
        binding.root.isEnabled = enable
    }

    private fun setEvent() {
        binding.btnMinus.setOnClickListener {
            if (counter > minCount)
                setCounter(counter - 1)
        }

        binding.btnPlus.setOnClickListener {
            if (counter < maxCount) {
                setCounter(counter + 1)
            }
        }
    }
}