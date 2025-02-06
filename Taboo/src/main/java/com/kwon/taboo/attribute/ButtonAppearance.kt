package com.kwon.taboo.attribute

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.GradientDrawable.RECTANGLE
import androidx.core.content.ContextCompat
import com.kwon.taboo.R
import com.kwon.taboo.button.TabooButton.Companion.BUTTON_SHAPE_RECT
import com.kwon.taboo.button.TabooButton.Companion.BUTTON_SHAPE_ROUNDED
import com.kwon.taboo.button.TabooButton.Companion.BUTTON_TYPE_SOLID
import com.kwon.taboo.button.TabooButton.Companion.BUTTON_TYPE_FILL
import com.kwon.taboo.button.TabooButton.Companion.BUTTON_TYPE_OUTLINE
import com.kwon.taboo.button.TabooButton.Companion.BUTTON_TYPE_DASH

class ButtonAppearance(
    private val context: Context,
    private val buttonShape: Int = BUTTON_SHAPE_RECT,
    private val buttonType: Int = BUTTON_TYPE_SOLID
) {
    private val gradientDrawable = GradientDrawable().apply {
        shape = RECTANGLE
    }

    fun create() : GradientDrawable {
        gradientDrawable.apply {
            cornerRadius = getButtonRadius()
            color = ContextCompat.getColorStateList(context, getDefaultBackgroundColor())
            setStroke(getButtonStroke())
        }
        return gradientDrawable
    }

    private fun getButtonRadius() : Float{
        val resources = context.resources
        return if (buttonShape == BUTTON_SHAPE_RECT) 0f else resources.getDimension(R.dimen.taboo_button_round_shape_radius)
    }

    private fun getDefaultBackgroundColor(): Int {
        return when (buttonType) {
            BUTTON_TYPE_SOLID -> R.color.selector_taboo_button_solid_type_background_color
            BUTTON_TYPE_FILL -> R.color.selector_taboo_button_fill_type_background_color
            BUTTON_TYPE_OUTLINE -> R.color.selector_taboo_button_outline_type_background_color
            BUTTON_TYPE_DASH -> R.color.selector_taboo_button_dash_type_background_color
            else -> 0
        }
    }

    private fun getButtonStroke() : Stroke {
        return Stroke(
            width = if (buttonType == BUTTON_TYPE_DASH || buttonType == BUTTON_TYPE_OUTLINE) 1 else 0,
            color = ContextCompat.getColorStateList(context, R.color.taboo_vibrant_blue_01),
            dashWidth = if (buttonType == BUTTON_TYPE_DASH) 5f else 0f,
            dashGap = if (buttonType == BUTTON_TYPE_DASH) 5f else 0f
        )
    }

    private fun GradientDrawable.setStroke(stroke: Stroke) {
        setStroke(stroke.width, stroke.color, stroke.dashWidth, stroke.dashGap)
    }
}