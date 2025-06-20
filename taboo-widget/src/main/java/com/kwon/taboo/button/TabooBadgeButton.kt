package com.kwon.taboo.button

import android.content.Context
import android.graphics.drawable.RippleDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.kwon.taboo.R
import com.kwon.taboo.numbering.TabooNumberingBall
import com.kwon.taboo.uicore.attribute.ButtonAppearance
import com.kwon.taboo.uicore.attribute.ColorContainer
import com.kwon.taboo.uicore.button.TabooButtonCore

class TabooBadgeButton(
    context: Context,
    attrs: AttributeSet
): TabooButtonCore(context, attrs) {
    private val rootView = LayoutInflater.from(context).inflate(R.layout.taboo_badge_button, this, true)

    private var badge: Int = 0             // 뱃지 숫자

    init {
        val typed = context.obtainStyledAttributes(attrs, R.styleable.TabooBadgeButton)
        val text = typed.getString(R.styleable.TabooBadgeButton_android_text) ?: ""
        val primaryColor = typed.getColor(R.styleable.TabooBadgeButton_primaryColor, ContextCompat.getColor(context, com.kwon.taboo.uicore.R.color.taboo_blue_700))
        val rippleColor = typed.getColor(R.styleable.TabooBadgeButton_rippleColor, ContextCompat.getColor(context, R.color.taboo_button_ripple_color))
        val badge = typed.getInt(R.styleable.TabooBadgeButton_badge, 0)

        typed.recycle()

        setText(text)
        setBadgeInternal(badge)

        setButtonAppearance(
            ButtonAppearance(
                context,
                ButtonAppearance.BUTTON_SHAPE_ROUNDED,
                ButtonAppearance.BUTTON_TYPE_SOLID,
                ColorContainer(primaryColor, 0),
                rippleColor
            )
        )

        isClickable = true

        initBadgeButton()
    }

    override fun drawButton() {
        this.background = createBackground()
    }

    override fun createBackground(): RippleDrawable {
        gradientDrawable.apply {
            cornerRadius = getRadius()
            color = getBackgroundColor()
            setStroke(getButtonStroke())
        }

        return RippleDrawable(getRippleColorState(), gradientDrawable, null)
    }

    /**
     * [TabooBadgeButton] UI 를 초기화.
     */
    private fun initBadgeButton() {
        updateBadge()
        updateText()

        drawButton()
    }

    /**
     * 뱃지 숫자를 설정한다.
     * @param badge 뱃지에 표시할 숫자
     */
    private fun setBadgeInternal(badge: Int) {
        this.badge = badge
    }

    fun getBadge() = badge

    /**
     * 뱃지 숫자를 설정하고, UI 를 업데이트한다.
     */
    fun setBadge(badge: Int) {
        setBadgeInternal(badge)

        updateBadge()
    }

    /**
     * 뱃지 숫자 UI 를 업데이트한다.
     */
    private fun updateBadge() {
        rootView.findViewById<TabooNumberingBall>(R.id.numbering_ball).text = badge.toString()
    }

    override fun updateText() {
        rootView.findViewById<TextView>(R.id.tv_button_text).text = getText()
    }
}