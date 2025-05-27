package com.kwon.taboo.button

import android.content.Context
import android.graphics.drawable.RippleDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes
import com.kwon.taboo.R
import com.kwon.taboo.databinding.TabooGhostButtonBinding
import com.kwon.taboo.uicore.button.TabooButtonCore

class TabooGhostButton(context: Context, attrs: AttributeSet): TabooButtonCore(context, attrs) {
    private val binding = TabooGhostButtonBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        context.withStyledAttributes(attrs, R.styleable.TabooGhostButton) {
            background = ContextCompat.getDrawable(
                context,
                getResourceId(
                    R.styleable.TabooIconButton_android_background,
                    R.drawable.selector_taboo_ghost_button
                )
            )
            getString(R.styleable.TabooGhostButton_android_text)?.let { text ->
                setText(text)
            }
            getResourceId(R.styleable.TabooGhostButton_android_src, 0).let { icon ->
                setIcon(icon)
            }
        }
    }

    fun setIcon(icon: Int) {
        binding.ivIcon.setImageResource(icon)
    }

    override fun setText(text: String) {
        binding.tvText.text = text
    }

    override fun createBackground(): RippleDrawable {
        gradientDrawable.apply {
            cornerRadius = getRadius()
            color = getBackgroundColor()
        }

        return RippleDrawable(
            getRippleColorState(),
            gradientDrawable,
            null
        )
    }

    override fun drawButton() {
        binding.root.background = createBackground()
    }
}