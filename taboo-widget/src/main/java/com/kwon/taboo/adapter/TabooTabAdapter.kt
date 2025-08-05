package com.kwon.taboo.adapter

import android.content.res.ColorStateList
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.kwon.taboo.R
import com.kwon.taboo.diffutils.TabooTabDiffCallback
import com.kwon.taboo.enums.PayLoad
import com.kwon.taboo.numbering.TabooNumberingBall
import com.kwon.taboo.tabs.TabooTabBlock

class TabooTabAdapter: ListAdapter<TabooTabBlock, TabooTabAdapter.TabooTabViewHolder>(TabooTabDiffCallback()) {
    private var selectedListener: TabSelectedListener? = null
    private var selectedTab: TabooTabBlock? = null

    private var tabPaddingLeft = 0
    private var tabPaddingRight = 0
    private var tabPaddingTop = 0
    private var tabPaddingBottom = 0

    private var tabFontFamily: Int = com.kwon.taboo.uicore.R.font.font_pretendard_medium
    private var tabTextSize: Float = 0f

    private var tabColorStateList: ColorStateList? = null
    private var ballColorStateList: ColorStateList? = null

    private var isVisibilityNumbering = false
    private var isVisibilityIcon = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TabooTabViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.taboo_tab, parent, false)
        return TabooTabViewHolder(view)
    }

    override fun onBindViewHolder(holder: TabooTabViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onBindViewHolder(holder: TabooTabViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
            return
        }

        payloads.forEach { payload ->
            when (payload) {
                // 선택된 탭 변경
                PayLoad.SELECTION_CHANGED -> {
                    holder.updateSelected(
                        isSelected = selectedTab?.uuid == currentList[position].uuid
                    )
                }
                // 숫자 표시 여부 변경
                PayLoad.NUMBERING_VISIBILITY_CHANGED -> {
                    holder.updateNumberingVisibility(isVisibilityNumbering)
                }
                // 아이콘 표시 여부 변경
                PayLoad.ICON_VISIBILITY_CHANGED -> {
                    holder.updateIconVisibility(isVisibilityIcon)
                }
                // 탭 폰트 패밀리 변경
                PayLoad.TAB_FONT_FAMILY_CHANGED -> {
                    holder.updateTabFontFamily()
                }
                PayLoad.TAB_TEXT_SIZE_CHANGED -> {
                    holder.updateTabTextSize()
                }
                // 탭 색상 변경
                PayLoad.TAB_COLOR_CHANGED -> {
                    holder.updateTabColor()
                }
                // 탭 내부 패딩 변경
                PayLoad.TAB_PADDING_CHANGED -> {
                    holder.updateTabPadding()
                }
                // Ball 색상 변경
                PayLoad.BALL_COLOR_CHANGED -> {
                    holder.updateBallColor()
                }
            }
        }
    }

    fun setTabPadding(padding: Int) {
        setTabPadding(padding, padding, padding, padding)
    }

    fun setTabPadding(left: Int, right: Int, top: Int, bottom: Int) {
        this.tabPaddingLeft = left
        this.tabPaddingRight = right
        this.tabPaddingTop = top
        this.tabPaddingBottom = bottom

        // Padding 변경
        notifyItemRangeChanged(0, itemCount, PayLoad.TAB_PADDING_CHANGED)
    }

    /**
     * 탭 이름 오른쪽에 `TabooNumberingBall`을 사용하여 표시하는 숫자를 표시할지 여부를 설정합니다.
     *
     * @param isVisibilityNumbering `true`: 숫자 표시, `false`: 숫자 미표시
     */
    fun isVisibilityNumbering(isVisibilityNumbering: Boolean) {
        this.isVisibilityNumbering = isVisibilityNumbering

        // Numbering Visibility 변경
        notifyItemRangeChanged(0, itemCount, PayLoad.NUMBERING_VISIBILITY_CHANGED)
    }

    /**
     * 숫자 표시 여부를 반환합니다.
     *
     * @return `true`: 숫자 표시, `false`: 숫자 미표시
     */
    fun isVisibilityNumbering(): Boolean {
        return isVisibilityNumbering
    }

    /**
     * 탭 아이콘을 표시할지 여부를 설정합니다.
     *
     * @param isVisibilityIcon `true`: 아이콘 표시, `false`: 아이콘 미표시
     */
    fun isVisibilityIcon(isVisibilityIcon: Boolean) {
        this.isVisibilityIcon = isVisibilityIcon

        notifyItemRangeChanged(0, itemCount, PayLoad.ICON_VISIBILITY_CHANGED)
    }

    /**
     * 아이콘 표시 여부를 반환합니다.
     *
     * @return `true`: 아이콘 표시, `false`: 아이콘 미표시
     */
    fun isVisibilityIcon(): Boolean {
        return isVisibilityIcon
    }

    fun setSelectedPosition(selectedPosition: Int) {
        val newSelectedTab = getItem(selectedPosition)

        // 이전 선택된 탭 비활성
        currentList
            .indexOfFirst { it.uuid == selectedTab?.uuid }
            .takeIf { it != -1 && it != selectedPosition }
            ?.let { prevSelectedPosition ->
                notifyItemChanged(prevSelectedPosition, PayLoad.SELECTION_CHANGED)
            }

        // 새로운 탭 저장
        this.selectedTab = newSelectedTab

        // 새로운 탭 선택
        notifyItemChanged(selectedPosition, PayLoad.SELECTION_CHANGED)

        // 선택된 탭의 position을 리스너에게 전달
        selectedListener?.onTabSelected(selectedPosition)
    }

    /**
     * 현재 선택된 탭의 위치를 반환합니다.
     *
     * 선택된 탭이 없으면 `-1`을 반환합니다.
     */
    fun getSelectedPosition(): Int = currentList.indexOfFirst { it.uuid == selectedTab?.uuid }

    fun setTabFontFamily(fontFamily: Int) {
        tabFontFamily = fontFamily
    }

    fun setTabTextSize(textSize: Float) {
        tabTextSize = textSize

        notifyItemRangeChanged(0, itemCount, PayLoad.TAB_TEXT_SIZE_CHANGED)
    }

    fun setTabColorStateList(tabColorStateList: ColorStateList) {
        this.tabColorStateList = tabColorStateList

        notifyItemRangeChanged(0, itemCount, PayLoad.TAB_COLOR_CHANGED)
    }

    fun setBallColorStateList(ballColorStateList: ColorStateList) {
        this.ballColorStateList = ballColorStateList

        notifyItemRangeChanged(0, itemCount, PayLoad.BALL_COLOR_CHANGED)
    }

    fun updateTab() {
        notifyItemRangeChanged(0, itemCount, PayLoad.TAB_FONT_FAMILY_CHANGED)
    }

    fun setTabSelectedListener(listener: TabSelectedListener) {
        this.selectedListener = listener
    }

    /**
     * 탭 뷰홀더
     */
    inner class TabooTabViewHolder(private val view: View): ViewHolder(view.rootView) {
        /**
         * 탭 데이터를 뷰에 바인딩.
         */
        fun bind(tab: TabooTabBlock) {
            view.findViewById<TextView>(R.id.tv_tab_title).text = tab.tabName
            view.findViewById<TabooNumberingBall>(R.id.tnb_count).text = tab.tabNumber.toString()
            if (tab.tabIcon != 0) {
                view
                    .findViewById<ImageView>(R.id.iv_tab_icon)
                    .setImageDrawable(ContextCompat.getDrawable(view.rootView.context, tab.tabIcon))
            }

            updateTabPadding()
            updateNumberingVisibility(isVisibilityNumbering)
            updateIconVisibility(isVisibilityIcon)
            updateTabColor()
            updateBallColor()
            updateTabFontFamily()
            updateTabTextSize()
            updateSelected(currentList[adapterPosition].uuid == selectedTab?.uuid)

            view.setOnClickListener {
                setSelectedPosition(adapterPosition)
            }
        }

        /**
         * 선택 여부에 따라 탭 업데이트.
         *
         * 선택된 탭은 `TabooBlue02` 색상으로 표시되고, 선택되지 않은 탭은 `TabooBlack03` 색상으로 표시됩니다.
         *
         * @param isSelected `true`: 선택된 탭, `false`: 선택되지 않은 탭
         */
        fun updateSelected(isSelected: Boolean) {
            view.findViewById<TextView>(R.id.tv_tab_title).isSelected = isSelected
            view.findViewById<TabooNumberingBall>(R.id.tnb_count).isSelected = isSelected
            view.findViewById<ImageView>(R.id.iv_tab_icon).isSelected = isSelected
        }

        /**
         * **숫자** 표시 여부에 따라 숫자 `Visibility` 업데이트.
         *
         * @param isVisibilityNumbering `true`: 숫자 표시, `false`: 숫자 미표시
         */
        fun updateNumberingVisibility(isVisibilityNumbering: Boolean) {
            view.findViewById<TabooNumberingBall>(R.id.tnb_count).visibility = if (isVisibilityNumbering) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }

        /**
         * **아이콘** 표시 여부에 따라 아이콘 `Visibility` 업데이트.
         *
         * @param isVisibilityIcon `true`: 아이콘 표시, `false`: 아이콘 미표시
         */
        fun updateIconVisibility(isVisibilityIcon: Boolean) {
            view.findViewById<ImageView>(R.id.iv_tab_icon).visibility =
                if (isVisibilityIcon && view.findViewById<ImageView>(R.id.iv_tab_icon).drawable != null) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
        }

        fun updateTabFontFamily() {
            view.findViewById<TextView>(R.id.tv_tab_title).setTypeface(
                ResourcesCompat.getFont(view.context, tabFontFamily)
            )
        }

        fun updateTabTextSize() {
            view.findViewById<TextView>(R.id.tv_tab_title).setTextSize(TypedValue.COMPLEX_UNIT_PX, tabTextSize)
        }

        fun updateTabColor() {
            tabColorStateList?.let { colorStateList ->
                view.findViewById<TextView>(R.id.tv_tab_title).setTextColor(colorStateList)
                view.findViewById<TabooNumberingBall>(R.id.tnb_count).setTextColor(colorStateList)
                view.findViewById<ImageView>(R.id.iv_tab_icon).drawable?.setTintList(colorStateList)
            }
        }

        fun updateTabPadding() {
            view.setPadding(tabPaddingLeft, tabPaddingTop, tabPaddingRight, tabPaddingBottom)
        }

        fun updateBallColor() {
            ballColorStateList?.let { colorStateList ->
                view.findViewById<TabooNumberingBall>(R.id.tnb_count).setBallColor(colorStateList)
            }
        }
    }

    interface TabSelectedListener {
        fun onTabSelected(index: Int)
    }
}
