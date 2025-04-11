package com.kwon.taboo.uicore.navigation.view

import android.content.Context
import android.content.res.ColorStateList
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.kwon.taboo.uicore.R
import com.kwon.taboo.uicore.navigation.model.TabooMenuItem

class TabooMenuItemView(context: Context): LinearLayout(context) {
    private var menuItem: TabooMenuItem? = null

    private var iconImageView: ImageView? = null
    private var menuTitleTextView: TextView? = null

    fun setMenuItem(menuItem: TabooMenuItem) {
        this.menuItem = menuItem

        updateMenu()
    }

    fun setIconTint(color: Int) {
        menuItem?.iconTint = ColorStateList.valueOf(color)
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
            it.imageTintList = menuItem?.titleTextColor
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
            it.setTextColor(menuItem?.iconTint)
        }

        removeAllViews()

        // 아이콘과 메뉴명을 배치
        addView(iconImageView)
        addView(menuTitleTextView)
    }
}