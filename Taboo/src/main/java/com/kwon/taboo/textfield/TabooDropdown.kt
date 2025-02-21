package com.kwon.taboo.textfield

import android.content.Context
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.ListPopupWindow
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.kwon.taboo.R
import com.kwon.taboo.adapter.TabooDropdownAdapter
import com.kwon.taboo.enums.AffixType

class TabooDropdown(
    private val context: Context,
    private val parent: ConstraintLayout
) : TabooTextField(context, parent) {
    private var ivDropdown: ImageView? = null

    private var adapter = TabooDropdownAdapter(
        context = context,
        layout = R.layout.taboo_dropdown_list_item
    ).apply {
        setDropDownViewResource(R.layout.taboo_dropdown_list_item)
    }

    private var listener: (() -> Unit)? = null

    private var itemSelectedListener: ((parent: AdapterView<*>?, view: View?, position: Int, id: Long) -> Unit)? = null
    private var itemChangedListener: ((position: Int) -> Unit)? = null

    init {
        setInputType(EditorInfo.TYPE_NULL)
    }

    private fun createDropdownIcon() {
        ivDropdown = ImageView(context).apply {
            setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_round_arrow_bottom))
            setDropdownIconColor(ContextCompat.getColor(context, R.color.taboo_gray_03))
            id = View.generateViewId()
        }
    }

    fun bindDropdownIcon() {
        if (ivDropdown == null) {
            createDropdownIcon()
        }

        bindView(ivDropdown!!, AffixType.SUFFIX)

        setDropdownClickListener()
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)

        ivDropdown?.isEnabled = enabled
    }

    fun setDropdownIcon(@DrawableRes icon: Int) {
        ivDropdown?.setImageDrawable(AppCompatResources.getDrawable(context, icon))
    }

    fun setDropdownIconColor(color: Int) {
        ivDropdown?.setColorFilter(color)
    }

    fun setItems(items: List<String>) {
        adapter.setItems(items)

        editText.text.clear()
    }

    fun setSelectedPosition(position: Int) {
        adapter.setSelectedPosition(position)

        editText.setText(adapter.getItem(position))

        itemChangedListener?.invoke(position)
    }

    fun setDropdownClickListener(listener: () -> Unit) {
        this.listener = listener
    }

    private fun setDropdownClickListener() {
        setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                view.performClick()
            }
        }

        setOnClickListener { v ->
            view.performClick()
        }

        ivDropdown?.setOnClickListener {
            view.performClick()
        }

        view.setOnClickListener {
            showPopupWindow()

            listener?.invoke()
        }
    }

    private fun showPopupWindow() {
        ListPopupWindow(context)
            .apply {
                setAnchorView(view)
                setModal(true)
                setPromptPosition(ListPopupWindow.POSITION_PROMPT_ABOVE)
                setAdapter(adapter)
                setOnItemClickListener({ parent, v, position, id ->
                    // 아이템 선택 & 변경 리스너 콜백
                    itemSelectedListener?.invoke(parent, v, position, id)
                    itemChangedListener?.invoke(position)

                    // 선택 아이템 포지션 저장 및 텍스트 설정
                    adapter.setSelectedPosition(position)
                    editText.setText(adapter.getItem(position))

                    // 팝업 닫기
                    dismiss()
                })
                width = this@TabooDropdown.getWidth()
                verticalOffset = 10
            }.show()
    }

    private fun getWidth() = view.width

    fun setOnItemSelectedListener(listener: ((parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) -> Unit)? = null) {
        itemSelectedListener = listener
    }

    fun setOnItemChangedListener(listener: ((position: Int) -> Unit)? = null) {
        itemChangedListener = listener
    }
}