package com.kwon.taboo.uicore.navigation

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.widget.LinearLayout
import com.hansae.taboo.core.util.ResourceUtils
import com.kwon.taboo.uicore.R
import com.kwon.taboo.uicore.attribute.ColorContainer
import com.kwon.taboo.uicore.attribute.Stroke
import com.kwon.taboo.uicore.navigation.view.TabooMenuItemView

open class TabooNavigationCore(context: Context, attrs: AttributeSet): LinearLayout(context, attrs) {
    private var menuResource = -1

    private var stroke: Stroke? = null

    private var menuItemColorContainer: ColorContainer? = null

    private var selectedMenuIndex = -1

    private var setMenuItemChangeListener: ((Int) -> Unit)? = null

    init {
        val typed = context.obtainStyledAttributes(attrs, R.styleable.TabooNavigationCore)

        val menuResource = typed.getResourceId(R.styleable.TabooNavigationCore_menu, -1)
        val menuItemPrimaryColor = typed.getColor(R.styleable.TabooNavigationCore_menuItemPrimaryColor, 0)
        val menuItemSecondaryColor = typed.getColor(R.styleable.TabooNavigationCore_menuItemSecondaryColor, 0)

        val strokeColor = typed.getColor(R.styleable.TabooNavigationCore_strokeColor, 0)
        val strokeWidth = typed.getDimension(R.styleable.TabooNavigationCore_strokeWidth, 1f)

        typed.recycle()

        setPadding(ResourceUtils.dpToPx(context, 8f), 0, ResourceUtils.dpToPx(context, 8f), 0)

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
            weightSum = menu.menus.size.toFloat()
            val menuOrientation = if (orientation == HORIZONTAL) VERTICAL else HORIZONTAL

            menu.menus.forEachIndexed { index, tabooMenuItem ->
                val menuItemView = TabooMenuItemView(context)
                menuItemView.setMenuItem(tabooMenuItem)
                menuItemView.setMenuOrientation(menuOrientation)

                addView(menuItemView)
                menuItemView.setWeight(1f)

                if (index != menu.menus.size - 1) {
                    menuItemView.setMarginEnd(ResourceUtils.dpToPx(context, 8f))
                }

                // 클릭 이벤트가 발생 시 현재 메뉴 아이템 외의 아이템은 isSelected을 false로 설정.
                menuItemView.setClickListener({
                    menu.menus.forEachIndexed { i, _ ->
                        selectedMenuIndex = i
                        (getChildAt(i) as TabooMenuItemView).isSelected(i == index)
                    }

                    setMenuItemChangeListener?.invoke(index)
                })
            }
        }
    }

    fun setMenuItemColorContainer(colorContainer: ColorContainer) {
        menuItemColorContainer = colorContainer
    }

    fun setStroke(stroke: Stroke) {
        this.stroke = stroke
    }

    fun setMenuItemChangeListener(listener: (Int) -> Unit) {
        setMenuItemChangeListener = listener
    }
}