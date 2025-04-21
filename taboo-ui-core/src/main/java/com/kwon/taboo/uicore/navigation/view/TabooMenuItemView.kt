package com.kwon.taboo.uicore.navigation.view

import android.content.Context
import android.content.res.ColorStateList
import android.util.Log
import android.util.TypedValue
import android.view.Gravity.CENTER
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.marginTop
import com.kwon.taboo.uicore.R
import com.kwon.taboo.uicore.navigation.model.TabooMenuItem
import com.kwon.taboo.uicore.util.ResourceUtils

class TabooMenuItemView(context: Context): LinearLayout(context) {
    private var menuItem: TabooMenuItem? = null

    private var iconImageView: ImageView? = null
    private var menuTitleTextView: TextView? = null

    private var menuTitleTextSize = 12f

    private var isSelected = false

    init {
        isClickable = true
        isFocusable = true
        gravity = CENTER
        isSelected(false)
    }

    fun isSelected(isSelected: Boolean) {
        this.isSelected = isSelected
        iconImageView?.isSelected = isSelected
        menuTitleTextView?.isSelected = isSelected
    }

    override fun isSelected(): Boolean {
        return isSelected
    }


    fun setMenuItem(menuItem: TabooMenuItem) {
        this.menuItem = menuItem

        updateMenu()
    }

    fun setMenuOrientation(orientation: Int) {
        this.orientation = orientation

        if (orientation == VERTICAL) {
            minimumHeight = context.resources.getDimensionPixelSize(R.dimen.taboo_menu_item_min_height)
        }
    }

    fun setIconTint(color: ColorStateList?) {
        menuItem?.iconTint = color

        updateMenuIconTint()
    }

    private fun updateMenuIconTint() {
        iconImageView?.imageTintList = menuItem?.iconTint
    }

    /**
     * setOrientation() 메서드를 오버라이드하여 [orientation]을 설정합니다.
     *
     * [orientation]이 변경되면 메뉴 아이템의 레이아웃을 업데이트합니다.
     */
    override fun setOrientation(orientation: Int) {
        super.setOrientation(orientation)

        updateMenu()
    }

    fun setWeight(weight: Float) {
        val params = layoutParams as LayoutParams
        params.weight = weight
        if (orientation == HORIZONTAL) {
            params.width = 0
            params.height = LayoutParams.WRAP_CONTENT
        } else {
            params.height = LayoutParams.WRAP_CONTENT
            params.width = 0
        }
    }

    fun setMarginStart(marginStart: Int) {
        val params = layoutParams as LayoutParams
        params.marginStart = marginStart
    }

    fun setMarginEnd(marginEnd: Int) {
        val params = layoutParams as LayoutParams
        params.marginEnd = marginEnd
    }

    private fun updateMenu() {
        if (iconImageView == null) {
            iconImageView = ImageView(context).apply {
                layoutParams = LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT
                )
            }
        }

        iconImageView?.let {
            it.setImageResource(menuItem?.iconResId ?: 0)
            it.imageTintList = menuItem?.iconTint
        }

        if (menuTitleTextView == null) {
            menuTitleTextView = TextView(context).apply {
                layoutParams = LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT
                )
            }
        }

        menuTitleTextView?.let {
            it.text = menuItem?.title ?: ""
            it.typeface = ResourcesCompat.getFont(context, R.font.font_pretendard_medium)
            it.setTextColor(menuItem?.iconTint)
            it.setTextSize(TypedValue.COMPLEX_UNIT_SP, menuTitleTextSize)
            it.setPadding(0, ResourceUtils.dpToPx(context, 3f), 0, 0)
        }

        removeAllViews()

        // 아이콘과 메뉴명을 배치
        addView(iconImageView)
        addView(menuTitleTextView)
    }

    fun setClickListener(listener: OnClickListener?) {
        setOnClickListener {
            listener?.onClick(this)
        }
    }
}