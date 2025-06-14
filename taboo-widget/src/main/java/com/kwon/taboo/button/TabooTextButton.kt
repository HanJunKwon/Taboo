package com.kwon.taboo.button

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.GradientDrawable.RECTANGLE
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.StyleRes
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.content.withStyledAttributes
import androidx.core.util.TypedValueCompat.ComplexDimensionUnit
import com.kwon.taboo.R
import com.kwon.taboo.uicore.TabooClickableViewCore
import com.kwon.taboo.uicore.util.ResourceUtils

class TabooTextButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
): TabooClickableViewCore(context, attrs, defStyle) {
    private val linearLayout = LinearLayout(context).apply {
        layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
        )
        gravity = Gravity.CENTER
        orientation = LinearLayout.HORIZONTAL
    }

    private val iconImageView = ImageView(context).apply {
        layoutParams = LayoutParams(
            ResourceUtils.dpToPx(context, IMAGE_VIEW_SIZE),
            ResourceUtils.dpToPx(context, IMAGE_VIEW_SIZE)
        )
        scaleType = ImageView.ScaleType.CENTER
    }

    private val textView = TextView(context).apply {
        layoutParams = LinearLayoutCompat.LayoutParams(0, LinearLayoutCompat.LayoutParams.WRAP_CONTENT, 1f)
        text = "Button"
        textAlignment = TEXT_ALIGNMENT_CENTER
        gravity = Gravity.CENTER
    }

    @IconPosition
    private var iconPosition = IconPosition.ICON_LEFT

    private var iconScaleType = IconScaleType.FIX

    private var contentSpace = CONTENT_SPACE_DP

    init {
        context.withStyledAttributes(attrs, R.styleable.TabooTextButton) {
            // 텍스트
            setText(getText(R.styleable.TabooTextButton_android_text).toString())

            // 텍스트 Appearance
            setTextAppearance(getResourceId(R.styleable.TabooTextButton_android_textAppearance, 0))
            
            // 텍스트 색상
            val textColor = getColorStateList(R.styleable.TabooTextButton_android_textColor)
                ?: ColorStateList.valueOf(ContextCompat.getColor(context, com.kwon.taboo.uicore.R.color.taboo_black_600))
            setTextColor(textColor)
            
            // 텍스트 사이즈
            setTextSize(
                unit = TypedValue.COMPLEX_UNIT_PX,
                textSize = getDimension(
                    R.styleable.TabooTextButton_android_textSize,
                    ResourceUtils.spToPx(context, TEXT_SIZE)
                )
            )
            
            // 폰트
            val fontResId = getResourceId(
                R.styleable.TabooTextButton_font,
                com.kwon.taboo.uicore.R.font.font_pretendard_medium // 기본값
            )
            setFont(ResourcesCompat.getFont(context, fontResId))

            // Icon
            setIcon(getResourceId(R.styleable.TabooTextButton_icon, 0))

            // Icon 배경 색상
            val iconBackgroundColor = getColorStateList(R.styleable.TabooTextButton_iconBackgroundColor)
                ?: ColorStateList.valueOf(ContextCompat.getColor(context, com.kwon.taboo.uicore.R.color.taboo_gray_100))
            setIconBackgroundColors(iconBackgroundColor)
            
            // Icon 색상
            val iconTint = getColorStateList(R.styleable.TabooTextButton_iconTint)
            setImageTintList(iconTint)

            // Icon Scale Type
            setIconScaleType(getInt(R.styleable.TabooTextButton_iconScaleType, IconScaleType.FIX))

            // Icon 위치
            setIconPosition(getInt(R.styleable.TabooTextButton_iconPosition, IconPosition.ICON_LEFT))

            setContentSpace(getDimension(R.styleable.TabooTextButton_contentSpace, CONTENT_SPACE_DP))
        }

        val paddingVertical = ResourceUtils.dpToPx(context, PADDING_VERTICAL_DP)
        val paddingHorizontal = ResourceUtils.dpToPx(context, PADDING_HORIZONTAL_DP)
        setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical)

        addView(
            linearLayout.apply {
                if (iconPosition == IconPosition.ICON_LEFT) {
                    addView(iconImageView)
                    addView(textView)
                } else {
                    addView(textView)
                    addView(iconImageView)
                }
            }
        )
    }

    fun setIcon(@DrawableRes iconDrawable: Int) {
        if (iconDrawable != 0) {
            iconImageView.setImageDrawable(ContextCompat.getDrawable(context, iconDrawable))
        }

        iconImageView.background = GradientDrawable().apply {
            shape = RECTANGLE
            cornerRadius = IMAGE_VIEW_CORNER_RADIUS
        }

        iconImageView.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, com.kwon.taboo.uicore.R.color.taboo_gray_100))
    }

    fun setIconBackgroundColors(colors: ColorStateList) {
        iconImageView.backgroundTintList = colors
    }

    private fun setIconScaleTypeInternal(@IconScaleType iconScaleType: Int) {
        this.iconScaleType = iconScaleType
    }

    fun setIconScaleType(@IconScaleType iconScaleType: Int) {
        setIconScaleTypeInternal(iconScaleType)

        updateIconLayoutParams()
    }

    private fun updateIconLayoutParams() {
        val params = iconImageView.layoutParams as LayoutParams
        iconImageView.layoutParams = params.apply {
            println("scaleType: $iconScaleType")
            val size = if (iconScaleType == IconScaleType.FIX) ResourceUtils.dpToPx(context, IMAGE_VIEW_SIZE) else LayoutParams.WRAP_CONTENT
            println("size: $size")
            width = size
            height = size
        }
    }

    fun setImageTintList(color: ColorStateList?) {
        if (color != null) {
            iconImageView.setImageTintList(color)
        }
    }

    fun setIconPosition(@IconPosition position: Int) {
        this.iconPosition = position

        updateContentSpace()
    }

    fun setText(text: String) {
        textView.text = text
    }

    fun setTextAppearance(@StyleRes resId: Int) {
        if (resId != 0) {
            textView.setTextAppearance(resId)
        }
    }

    fun setTextColor(@ColorInt color: Int) {
        textView.setTextColor(color)
    }

    fun setTextColor(colors: ColorStateList) {
        textView.setTextColor(colors)
    }

    fun setTextSize(@ComplexDimensionUnit unit: Int = TypedValue.COMPLEX_UNIT_SP, textSize: Float) {
        textView.setTextSize(unit, textSize)
    }

    fun setFont(tf: Typeface?) {
        textView.typeface = tf
    }

    fun setContentSpace(space: Float) {
        this.contentSpace = space

        updateContentSpace()
    }

    private fun updateContentSpace() {
        val margin = ResourceUtils.dpToPx(context, contentSpace)

        (textView.layoutParams as? LinearLayout.LayoutParams)?.let { lp ->
            lp.marginStart = if (iconPosition == IconPosition.ICON_LEFT) margin else 0
            lp.marginEnd = if (iconPosition == IconPosition.ICON_RIGHT) margin else 0
            textView.layoutParams = lp
        }
    }

    annotation class IconScaleType {
        companion object {
            const val FIX = 0
            const val FIT = 1
        }
    }

    annotation class IconPosition {
        companion object {
            const val ICON_LEFT = 0
            const val ICON_RIGHT = 1
        }
    }

    companion object {
        const val PADDING_VERTICAL_DP = 8f
        const val PADDING_HORIZONTAL_DP = 16f

        const val IMAGE_VIEW_SIZE = 36f
        const val IMAGE_VIEW_CORNER_RADIUS = 20f

        const val TEXT_SIZE = 16f

        const val CONTENT_SPACE_DP = 10f
    }
}