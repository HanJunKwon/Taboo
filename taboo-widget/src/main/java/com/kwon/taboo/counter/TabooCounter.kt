package com.kwon.taboo.counter

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.kwon.taboo.R

class TabooCounter(context: Context, attrs: AttributeSet): ConstraintLayout(context, attrs) {
    private val rootView = LayoutInflater.from(context).inflate(R.layout.taboo_counter, this, true)

    /**
     * Counter 값
     */
    private var count = 0

    /**
     * 최소 Counter 값
     */
    private var minCount = 0

    /**
     * 최대 Counter 값
     */
    private var maxCount = Int.MAX_VALUE

    /**
     * Minus 버튼 아이콘 Tint
     */
    private var minusIconTint: ColorStateList? = null

    /**
     * Plus 버튼 아이콘 Tint
     */
    private var plusIconTint: ColorStateList? = null

    private var enabled = true

    private var onCountClickListener: OnCountClickListener? = null
    private var onCountChangeListener: OnCountChangeListener? = null

    init {
        val typed = context.obtainStyledAttributes(attrs, R.styleable.TabooCounter)
        val minCount = typed.getInt(R.styleable.TabooCounter_minCount, 0)
        val maxCount = typed.getInt(R.styleable.TabooCounter_maxCount, Int.MAX_VALUE)
        val minusIconTintList = typed.getColorStateList(R.styleable.TabooCounter_minusIconTint) ?: ContextCompat.getColorStateList(context, R.color.selector_taboo_counter_icon)
        val plusIconTintList = typed.getColorStateList(R.styleable.TabooCounter_plusIconTint) ?: ContextCompat.getColorStateList(context, R.color.selector_taboo_counter_icon)
        val enabled = typed.getBoolean(R.styleable.TabooCounter_android_enabled, true)

        typed.recycle()

        setCount(0)
        setMinCount(minCount)
        setMaxCount(maxCount)
        setMinusIconTintList(minusIconTintList)
        setPlusIconTint(plusIconTintList)
        isEnabled = enabled

        setEvent()
    }

    /**
     * Counter 값을 설정합니다.
     */
    fun setCount(value: Int) {
        count = value
        updateCount()
    }

    /**
     * Counter 값을 반환합니다.
     */
    fun getCount(): Int {
        return count
    }

    /**
     * Counter UI를 업데이트합니다.
     */
    private fun updateCount() {
        rootView.findViewById<TextView>(R.id.tv_count).text = count.toString()

        onCountChangeListener?.onCountChanged(count)
    }

    /**
     * 최소 Counter 값을 설정합니다.
     *
     * [count]가 설정하라는 값보다 작더라도, [count]는 변경되지 않습니다.
     * [TabooCounter]에서 관리되는 값보다 개발자가 관리하는 값을 우선 시하기 위함입니다.
     * 만약 [minCount]가 변경됨에 따라 [count]를 변경하고 싶으면
     * [setCount]를 사용하여 변경하시기 바랍니다.
     */
    fun setMinCount(value: Int) {
        minCount = value
    }

    /**
     * 최대 Counter 값을 설정합니다.
     *
     * [count]가 설정하라는 값보다 크더라도, [count]는 변경되지 않습니다.
     * [TabooCounter]에서 관리되는 값보다 개발자가 관리하는 값을 우선 시하기 위함입니다.
     * 만약 [maxCount]가 변경됨에 따라 [count]를 변경하고 싶으면
     * [setCount]를 사용하여 변경하시기 바랍니다.
     */
    fun setMaxCount(value: Int) {
        maxCount = value
    }

    /**
     * Minus 버튼 아이콘 Tint를 설정합니다.
     */
    fun setMinusIconTintList(tintList: ColorStateList?) {
        this.minusIconTint = tintList
        updateMinusIconTint()
    }

    /**
     * Minus 버튼 아이콘 Tint를 반환합니다.
     */
    fun getMinusIconTint(): ColorStateList? {
        return minusIconTint
    }

    /**
     * Minus 버튼 아이콘 Tint를 업데이트합니다.
     */
    private fun updateMinusIconTint() {
        rootView.findViewById<ImageView>(R.id.btn_minus).imageTintList = minusIconTint
    }

    /**
     * Plus 버튼 아이콘 Tint를 설정합니다..
     */
    fun setPlusIconTint(tintList: ColorStateList?) {
        plusIconTint = tintList
        updatePlusIconTint()
    }

    /**
     * Plus 버튼 아이콘 Tint를 반환합니다.
     */
    fun getPlushIconTint(): ColorStateList? {
        return plusIconTint
    }

    /**
     * Plus 버튼 아이콘 Tint를 업데이트합니다.
     */
    private fun updatePlusIconTint() {
        rootView.findViewById<ImageView>(R.id.btn_plus).imageTintList = plusIconTint
    }

    /**
     * Counter를 활성화/비활성화 상태를 설정합니다.
     */
    override fun setEnabled(enable: Boolean) {
        super.setEnabled(enable)

        rootView.findViewById<ImageView>(R.id.btn_minus).isEnabled = enable
        rootView.findViewById<ImageView>(R.id.btn_plus).isEnabled = enable
        rootView.findViewById<TextView>(R.id.tv_count).isEnabled = enable
    }

    /**
     * Counter가 활성화/비활성화 상태를 반환합니다.
     */
    override fun isEnabled(): Boolean {
        return enabled
    }

    private fun setEvent() {
        rootView.findViewById<ImageView>(R.id.btn_minus).setOnClickListener {
            if (count > minCount)
                setCount(count - 1)

            onCountClickListener?.onMinusClicked()
        }

        rootView.findViewById<ImageView>(R.id.btn_plus).setOnClickListener {
            if (count < maxCount)
                setCount(count + 1)

            onCountClickListener?.onPlusClicked()
        }
    }

    fun setOnCountClickListener(listener: OnCountClickListener?) {
        this.onCountClickListener = listener
    }

    fun setOnCountChangeListener(listener: OnCountChangeListener?) {
        this.onCountChangeListener = listener
    }

    /**
     * Minus 또는 Plus 버튼을 클릭했을 때 호출되는 리스너입니다.
     */
    interface OnCountClickListener {
        /**
         * Minus 버튼이 클릭되었을 때 호출됩니다.
         * @
         */
        fun onMinusClicked()

        /**
         * Plus 버튼이 클릭되었을 때 호출됩니다.
         */
        fun onPlusClicked()
    }

    interface OnCountChangeListener {
        /**
         * Counter 값이 변경되었을 때 호출됩니다.
         */
        fun onCountChanged(count: Int)
    }
}