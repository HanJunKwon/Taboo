package com.kwon.taboo.uicore.navigation

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.XmlResourceParser
import android.util.AttributeSet
import android.util.Log
import android.util.Xml
import androidx.annotation.MenuRes
import androidx.core.content.ContextCompat
import com.kwon.taboo.uicore.R
import com.kwon.taboo.uicore.navigation.model.TabooMenu
import com.kwon.taboo.uicore.navigation.model.TabooMenuItem
import org.xmlpull.v1.XmlPullParser.END_TAG
import org.xmlpull.v1.XmlPullParser.START_TAG

/**
 * [TabooMenuParser]는 XML 메뉴 리소스를 파싱하여 [TabooMenu] 객체를 생성하는 클래스입니다.
 * 메뉴 리소스에서 사용가능한 속성은 다음과 같습니다.
 * - android:id : 메뉴 아이템의 ID
 * - android:title : 메뉴 아이템의 제목
 * - android:titleTextColor : 메뉴 아이템의 제목 색상
 */
class TabooMenuParser(private val context: Context) {

    /**
     * XML 메뉴 리소스를 파싱하기 위한 메서드입니다.
     * 외부 클래스에서 메뉴 리소스를 파싱하기 위해서는 이 메서드를 사용해야합니다.
     */
    fun parse(@MenuRes menuRes: Int): TabooMenu {
        Log.d(">>>", "menuRes: $menuRes")

        @SuppressLint("ResourceType")
        val xmlParser = context.resources.getLayout(menuRes)
        val attrs = Xml.asAttributeSet(xmlParser)

        return parseMenu(xmlParser, attrs)
    }

    private fun parseMenu(
        parser: XmlResourceParser,
        attrs: AttributeSet
    ): TabooMenu {
        val items = mutableListOf<TabooMenuItem>()
        var eventType = parser.eventType
        var isEndOfMenu = false

        while (!isEndOfMenu) {
            val name = parser.name
            when {
                eventType == START_TAG && name == XML_MENU_ITEM_TAG -> {
                    // menu item 속성 파싱 시작.
                    val menuItem = parseMenuItem(attrs)
                    items.add(menuItem)
                }
                eventType == END_TAG && name == XML_MENU_TAG -> {
                    // XML 파싱 종료
                    isEndOfMenu = true
                }
            }

            // 다음 이벤트로 이동
            eventType = parser.next()
        }

        return TabooMenu(items)
    }

    private fun parseMenuItem(attrs: AttributeSet): TabooMenuItem {
        val sAttr = context.obtainStyledAttributes(attrs, R.styleable.TabooMenuItem)
        val id = sAttr.getResourceId(R.styleable.TabooMenuItem_android_id, -1)
        val title = sAttr.getString(R.styleable.TabooMenuItem_android_title) ?: ""
        val titleTextColor = sAttr.getColorStateList(R.styleable.TabooMenuItem_android_titleTextColor) ?:
            ContextCompat.getColorStateList(context, R.color.taboo_menu_item_title)
        val icon = sAttr.getResourceId(R.styleable.TabooMenuItem_android_icon, 0)
        val iconColor = sAttr.getColorStateList(R.styleable.TabooMenuItem_iconColor) ?:
            ContextCompat.getColorStateList(context, R.color.taboo_menu_item_icon_tint)

        sAttr.recycle()

        return TabooMenuItem(id, title, titleTextColor, icon, iconColor)
    }

    companion object {
        private const val XML_MENU_TAG = "menu"
        private const val XML_MENU_ITEM_TAG = "item"
    }
}