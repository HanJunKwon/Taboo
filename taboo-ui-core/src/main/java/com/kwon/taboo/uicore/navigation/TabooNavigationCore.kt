package com.kwon.taboo.uicore.navigation

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.util.Log
import android.util.Xml
import android.widget.LinearLayout
import com.kwon.taboo.uicore.R
import com.kwon.taboo.uicore.attribute.ColorContainer
import com.kwon.taboo.uicore.attribute.Stroke
import com.kwon.taboo.uicore.navigation.view.TabooMenuItemView
import com.kwon.taboo.uicore.util.ResourceUtils

open class TabooNavigationCore(context: Context, attrs: AttributeSet): LinearLayout(context, attrs) {
    private var menuResource = -1

    private var stroke: Stroke? = null

    private var menuItemColorContainer: ColorContainer? = null

    init {
        val typed = context.obtainStyledAttributes(attrs, R.styleable.TabooNavigationCore)

        val menuResource = typed.getResourceId(R.styleable.TabooNavigationCore_menu, -1)
        val menuItemPrimaryColor = typed.getColor(R.styleable.TabooNavigationCore_menuItemPrimaryColor, 0)
        val menuItemSecondaryColor = typed.getColor(R.styleable.TabooNavigationCore_menuItemSecondaryColor, 0)

        val strokeColor = typed.getColor(R.styleable.TabooNavigationCore_strokeColor, 0)
        val strokeWidth = typed.getDimension(R.styleable.TabooNavigationCore_strokeWidth, 1f)

        typed.recycle()

        setMenuItemColorContainer(
            ColorContainer(
                primaryColor = menuItemPrimaryColor,
                secondaryColor = menuItemSecondaryColor
            )
        )

        setStroke(
            Stroke(
                color = ColorStateList(
                    arrayOf(intArrayOf()),
                    intArrayOf(strokeColor)
                ),
                width = ResourceUtils.dpToPx(context, strokeWidth),
            )
        )

        setMenuResource(menuResource)

        draw()
    }

    private fun draw() {
        updateMenu()
    }

    fun setMenuResource(menuResource: Int) {
        this.menuResource = menuResource

        updateMenu()
    }

    private fun updateMenu() {
        if (menuResource == -1) {
            throw IllegalStateException("Menu resource is not set.")
        }

        removeAllViews()

        TabooMenuParser(context).parse(menuResource).let { menu ->
            menu.menus.forEach { item ->
                val menuItemView = TabooMenuItemView(context)
                menuItemView.setMenuItem(item)

                addView(menuItemView)
            }
        }
    }

    fun setMenuItemColorContainer(colorContainer: ColorContainer) {
        menuItemColorContainer = colorContainer
    }

    fun setStroke(stroke: Stroke) {
        this.stroke = stroke
    }
}