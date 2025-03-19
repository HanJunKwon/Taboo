package com.kwon.taboo.textview

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity.CENTER
import androidx.annotation.ColorInt
import androidx.annotation.FontRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.kwon.taboo.R
import com.kwon.taboo.attribute.ColorContainer
import com.kwon.utils.calendar.ResourceUtils


class TabooPillTag(
    context: Context,
    attrs: AttributeSet
): AppCompatTextView(context, attrs) {
    private var radius = ResourceUtils.dpToPx(context, 20f)
    private val colorContainer = ColorContainer(
        primaryColor = ContextCompat.getColor(context, R.color.taboo_vibrant_blue_01),
        secondaryColor = ContextCompat.getColor(context, R.color.taboo_blue_06)
    )

    private val defaultTextSize = 10f

    private val horizontalPadding = 10
    private val verticalPadding = 5

    private val minimumHeightDp = 25
    private val minimumWidthDp = 80

    init {
        val typed = context.obtainStyledAttributes(attrs, R.styleable.TabooPillTag)

        val primaryColor = typed.getColor(R.styleable.TabooPillTag_primaryColor, ContextCompat.getColor(context, R.color.taboo_vibrant_blue_01))
        val secondaryColor = typed.getColor(R.styleable.TabooPillTag_secondaryColor, ContextCompat.getColor(context, R.color.taboo_blue_06))
        val radius = typed.getDimension(R.styleable.TabooPillTag_radius, 20f)

        setPillColorInternal(primaryColor, secondaryColor)
        setPillRadiusInternal(radius)

        initTabooPillTag(typed)

        typed.recycle()
    }

    private fun initTabooPillTag(typed: TypedArray) {
        updatePill()

        val fontFamilyResId = typed.getResourceId(R.styleable.TabooPillTag_android_fontFamily, 0)
        setTypeface(fontFamilyResId)

        val textColor = typed.getColorStateList(R.styleable.TabooPillTag_android_textColor)
        setTextColor(textColor)

        if (typed.hasValue(R.styleable.TabooPillTag_android_textSize)) {
            val typedValue = TypedValue()
            val isNotEmptyTextSize = typed.getValue(R.styleable.TabooPillTag_android_textSize, typedValue)
            if (isNotEmptyTextSize && typedValue.type == TypedValue.TYPE_DIMENSION) {
                setTextSize(TypedValue.COMPLEX_UNIT_PX, typed.getDimension(R.styleable.TabooPillTag_android_textSize, 0f))
            } else {
                setTextSize(TypedValue.COMPLEX_UNIT_SP, defaultTextSize)
            }
        } else {
            setTextSize(TypedValue.COMPLEX_UNIT_SP, defaultTextSize)
        }

        alignTextCenter()

        setPadding(horizontalPadding, verticalPadding, horizontalPadding, verticalPadding)

        val density = context.resources.displayMetrics.density
        minimumHeight = Math.round(minimumHeightDp * density)
        minimumWidth = Math.round(minimumWidthDp * density)
    }

    override fun setTextColor(colors: ColorStateList?) {
        val textColor = colors ?: colorContainer.getPrimaryColorStateList()
        super.setTextColor(textColor)
    }

    fun setTypeface(@FontRes fontFamilyResId: Int) {
        val typeface = if (fontFamilyResId == 0) null else ResourcesCompat.getFont(context, fontFamilyResId)
        setTypeface(typeface)
    }

    override fun setTypeface(tf: Typeface?) {
        super.setTypeface(tf ?: ResourcesCompat.getFont(context, R.font.font_pretendard_semi_bold))
    }

    private fun setPillColorInternal(@ColorInt primaryColor: Int, @ColorInt secondaryColor: Int) {
        colorContainer.primaryColor = primaryColor
        colorContainer.secondaryColor = secondaryColor
    }

    fun setPillColor(@ColorInt primaryColor: Int, @ColorInt secondaryColor: Int) {
        setPillColorInternal(primaryColor, secondaryColor)
        setTextColor(colorContainer.getPrimaryColorStateList())

        updatePill()
    }

    private fun updatePill() {
        val background = getMakePillBackgroundDrawable()
        setBackgroundDrawable(background)
    }

    private fun setPillRadiusInternal(radius: Float) {
        this.radius = ResourceUtils.dpToPx(context, radius)
    }

    fun setPillRadius(radius: Float) {
        setPillRadiusInternal(radius)

        updatePill()
    }

    private fun getMakePillBackgroundDrawable(): GradientDrawable {
        val drawable = GradientDrawable()
        drawable.cornerRadius = radius
        drawable.setStroke(2, colorContainer.getPrimaryColorStateList())
        drawable.color = colorContainer.getSecondaryColorStateList()
        return drawable
    }

    /**
     * 텍스트를 중앙에 맞추기
     */
    private fun alignTextCenter() {
        textAlignment = TEXT_ALIGNMENT_CENTER
        gravity = CENTER
    }
}