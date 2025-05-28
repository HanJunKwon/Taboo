package com.kwon.taboo.metrics

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.kwon.taboo.R
import com.kwon.taboo.enums.Sign
import java.text.DecimalFormat

class MetricCard(
    context: Context,
    attrs: AttributeSet
) : ConstraintLayout(context, attrs) {

    companion object {
        const val VALUE_TYPE_INTEGER = 0
        const val VALUE_TYPE_DECIMAL = 1

        const val UNIT_TYPE_NONE = 0
        const val UNIT_TYPE_PERCENTAGE = 1
        const val UNIT_TYPE_CUSTOM = 2
    }

    private val rooView = LayoutInflater.from(context).inflate(R.layout.taboo_metric_card, this, true)

    private var title: String = ""

    private var prevValue: Number = 0
    private var value: Number = 0
    private var valueType = VALUE_TYPE_INTEGER

    private var prevPoint: Float = 0f
    private var point: Float = 0f
    private var pointSign: Sign = Sign.ZERO

    private var unitType: Int = UNIT_TYPE_NONE
    private var unit: String = ""

    private var valueAnimator = ValueAnimator()
    private var pointAnimator = ValueAnimator()

    private val decimalExpression = DecimalFormat("##.##")      // 실수 표현식
    private val integerExpression = DecimalFormat("#,###")      // 정수 표현식

    private val zeroColor = com.kwon.taboo.uicore.R.color.taboo_opacity_black_b_89
    private val increaseColor = com.kwon.taboo.uicore.R.color.taboo_blue_600
    private val decreaseColor = com.kwon.taboo.uicore.R.color.taboo_blue_500

    init {
        val typed       = context.obtainStyledAttributes(attrs, R.styleable.MetricCard)
        val title       = typed.getString(R.styleable.MetricCard_metricTitle) ?: "타이틀"
        val valueType   = typed.getInt(R.styleable.MetricCard_metricValueType, VALUE_TYPE_INTEGER)
        val value       = typed.getFloat(R.styleable.MetricCard_metricValue, 0f)
        val point       = typed.getFloat(R.styleable.MetricCard_metricPoint, 0f)

        val unitType = typed.getInt(R.styleable.MetricCard_unitType, UNIT_TYPE_NONE)
        val unit = when (unitType) {
            UNIT_TYPE_PERCENTAGE -> "%"
            UNIT_TYPE_CUSTOM -> typed.getString(R.styleable.MetricCard_unit) ?: ""
            else -> ""
        }

        typed.recycle()

        setTitle(title)
        setValueInternal(value, valueType)
        setPointInternal(point)
        setUnit(unit)

        initUpdateMetricCard()
    }

    private fun initUpdateMetricCard() {
        updateTitle()
        updateValue()
        updatePoint()
    }

    /**
     * 지표 제목을 설정한다.
     */
    fun setTitle(title: String) {
        this.title = title
    }

    /**
     * 지표 제목 UI를 업데이트한다.
     */
    private fun updateTitle() {
        rootView.findViewById<TextView>(R.id.tv_title).text = title
    }

    /**
     * 지표 제목을 반환한다.
     */
    fun getTitle() = title

    /**
     * 내부적으로 지표 값을 설정하는 메서드.
     *
     * - 지표 값이 변경될 때 애니메이션 효과를 적용하기 위해 이전 값을 [prevValue]에 저장한다.
     * - 현재 설정된 지표 값은 [value]에 저장된다.
     * - 값의 자료형을 구분하기 위해 [valueType]을 함께 저장한다.
     *
     * @param value 새로운 지표 값 (Number 타입)
     * @param valueType 지표 값의 자료형을 나타내는 Int 값 ([VALUE_TYPE_INTEGER], [VALUE_TYPE_DECIMAL])
     */
    private fun setValueInternal(value: Number, valueType: Int) {
        this.prevValue = this.value
        this.value = value
        this.valueType = valueType
    }

    /**
     * 지표 값을 설정한다.
     */
    fun setValue(value: Number) {
        setValueInternal(value, this.valueType)

        updateValue()
    }

    /**
     * 지표 값 UI를 업데이트한다.
     *
     * 값 변경 시 애니메이션을 적용하기 위해서 [ValueAnimator]를 사용한다.
     * 애니메이션되는 지표 값의 표현식을 구분하기 위해서 [valueType]을 사용하고, [VALUE_TYPE_INTEGER], [VALUE_TYPE_DECIMAL]를
     * 중 하나를 선택하여 지정한다.
     * - see: [setValueType] - 지표 값 자료형 변경 메서드
     */
    private fun updateValue() {
        valueAnimator = when (valueType) {
            VALUE_TYPE_DECIMAL -> ValueAnimator.ofFloat(prevValue.toFloat(), value.toFloat())
            else -> ValueAnimator.ofInt(prevValue.toInt(), value.toInt())
        }.apply {
            duration = 500
            interpolator = AccelerateDecelerateInterpolator()
            addUpdateListener {
                val express = when (valueType) {
                    VALUE_TYPE_DECIMAL -> decimalExpression
                    else -> integerExpression
                }
                rootView.findViewById<TextView>(R.id.tv_value).text = "${express.format(it.animatedValue)}$unit"
            }
            start()
        }
    }

    /**
     * 지표 값을 반환한다.
     */
    fun getValue() = value

    /**
     * 지표 값의 자료형을 설정한다.
     * @param valueType 지표 값의 자료형 ([VALUE_TYPE_DECIMAL]]
     */
    fun setValueType(valueType: Int) {
        this.valueType = valueType

        updateValueType()
    }

    /**
     * 지표 값 자료형에 따라 지표 값 UI를 업데이트한다. 이 때는 애니메이션 효과를 적용하지 않고 즉시 UI를 업데이트한다.
     */
    private fun updateValueType() {
        val expression = when (valueType) {
            VALUE_TYPE_INTEGER -> integerExpression
            else -> decimalExpression
        }

        rootView.findViewById<TextView>(R.id.tv_value).text = "${expression.format(value)}$unit"
    }

    /**
     * 내부적으로 **지표 값의 변동률**을 설정한다.
     *
     * - 지표 값이 변경될 때 애니메이션 효과를 적용하기 위해 이전 값을 [prevValue]에 저장한다.
     * - 현재 설정된 지표 값은 [value]에 저장된다.
     * - 값의 자료형을 구분하기 위해 [valueType]을 함께 저장한다.
     *
     * @param point - 새로운 지표 값의 변동률 (Number 타입)
     * see: [setPoint] - 지표 값 변동률 설정 메서드.
     */
    private fun setPointInternal(point: Float) {
        this.prevPoint = this.point
        this.point = point
        this.pointSign = when {
            point > 0f -> Sign.PLUS
            point < 0f -> Sign.MINUS
            else -> Sign.ZERO
        }

    }

    /**
     * **지표 값의 변동률**을 설정한다.
     */
    fun setPoint(point: Float) {
        setPointInternal(point)

        updatePoint()
    }

    /**
     * 지표 값의 변동률에 따른 UI를 업데이트한다.
     *
     * - 지표 값 변동률 변경 애니메이션
     * - 지표 값 변동률에 따른 화살표 표시
     * - 지표 값 변동률에 따른 텍스트 및 화살표 색상 변경
     */
    private fun updatePoint() {
        updatePointColor()      // 포인트 수치 업데이트
        updatePointArrow()      // 포인트 화살표 업데이트

        pointAnimator = ValueAnimator.ofFloat(prevPoint, point).apply {
            duration = 500
            interpolator = AccelerateDecelerateInterpolator()
            addUpdateListener {
                rootView.findViewById<TextView>(R.id.tv_point).text = "${decimalExpression.format(it.animatedValue)}%"
            }
            start()
        }
    }

    /**
     * 현재 [point]를 반환한다.
     */
    fun getPoint() = point

    /**
     * [point] 부호에 맞는 포인트 색상 UI를 업데이트한다.
     */
    private fun updatePointColor() {
        rootView.findViewById<TextView>(R.id.tv_point).setTextColor(getPointSignColor(pointSign))
    }

    /**
     * [point] 부호에 맞는 화살표 이미지로 UI를 업데이트한다.
     */
    private fun updatePointArrow() {
        rootView.findViewById<ImageView>(R.id.ic_point_arrow).setImageDrawable(getPointArrow(pointSign))
    }

    /**
     * [pointSign]에 따라 화살표 아이콘을 반환하는 메서드.
     *
     * @param pointSign 부호(Sign) 값 ([Sign.PLUS], [Sign.MINUS] 또는 [Sign.ZERO])
     * @return 지정된 부호에 해당하는 화살표 Drawable 객체, 부호가 `PLUS`이면 위쪽 화살표,
     * `MINUS`이면 아래쪽 화살표를 반환하며, 그 외의 경우 `null` 반환.
     */
    private fun getPointArrow(pointSign: Sign): Drawable? {
        // 화살표 방향
        val arrow = when (pointSign) {
            Sign.PLUS -> R.drawable.ic_round_top_arrow_24
            Sign.MINUS -> R.drawable.ic_round_bottom_arrow_24
            else -> return null
        }

        // 화살표 색상
        val arrowDrawable = ContextCompat.getDrawable(context, arrow)
        val pointSignColor = getPointSignColor(pointSign)
        arrowDrawable?.setTint(pointSignColor)

        return arrowDrawable
    }

    /**
     * [pointSign] 음수, 양수, 0 여부에 따라 지정된 색상을 반환하는 메서드.
     * - 양수: [increaseColor]
     * - 음수: [decreaseColor]
     * - 0: [zeroColor]
     */
    private fun getPointSignColor(pointSign: Sign): Int {
        val color = when (pointSign) {
            Sign.PLUS -> increaseColor
            Sign.MINUS -> decreaseColor
            Sign.ZERO -> zeroColor
        }

        return ContextCompat.getColor(context, color)
    }

    /**
     * 지표 값의 단위를 설정한다.
     *
     * - [UNIT_TYPE_NONE] : 단위 사용하지 않음.
     * - [UNIT_TYPE_PERCENTAGE] : 퍼센트 사용
     * - [UNIT_TYPE_CUSTOM] : 단위를 커스텀하여 사용.
     * - see: [setUnit] - 단위를 커스텀하여 사용하는 경우 해당 메서드를 사용하여 단위를 지정한다.
     */
    fun setUnitType(unitType: Int) {
        this.unitType = unitType

        updateUnitType()
    }

    /**
     * 지표 값 단위 UI를 업데이트한다.
     */
    private fun updateUnitType() {
        when (unitType) {
            UNIT_TYPE_NONE -> unit = ""
            UNIT_TYPE_PERCENTAGE -> unit = "%"
            UNIT_TYPE_CUSTOM -> return
        }
    }

    /**
     * 지표 단위 타입을 반환한다.
     */
    fun getUnitType() = unitType

    /**
     * 지표 값 단위 타입을 [UNIT_TYPE_CUSTOM]으로 지정하면 해당 단위가 표시된다.
     *
     * - see: [setUnitType] - 단위 타입을 설정하는 메서드.
     */
    fun setUnit(unit: String) {
        this.unit = unit
    }

    /**
     * 지표 단위를 반환한다.
     */
    fun getUnit() = unit

}