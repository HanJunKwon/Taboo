package com.kwon.taboo.textfield

import android.content.Context
import android.view.LayoutInflater
import android.widget.AdapterView
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListPopupWindow
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import com.kwon.taboo.R
import com.kwon.taboo.adapter.TabooDropdownAdapter

class TabooDropdown(
    private val context: Context,
    parent: ConstraintLayout
) {
    private val view = LayoutInflater.from(context).inflate(R.layout.taboo_dropdown, parent, true)
    private val editText = view.findViewById<EditText>(R.id.edt_text)
    private val ivDropdown = view.findViewById<ImageView>(R.id.iv_dropdown)

    private var adapter: TabooDropdownAdapter? = null

    private var listener: (() -> Unit)? = null
    private var items = arrayOf<String>()

    private var itemSelectedListener: ((parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) -> Unit)? = null
    private var itemChangedListener: ((position: Int) -> Unit)? = null

    init {
        setDropdownClickListener()
    }

    fun setText(text: String) {
        editText.setText(text)
    }

    fun getText(): String {
        return editText.text.toString()
    }

    fun setHint(hint: String) {
        editText.hint = hint
    }

    fun setEnabled(enabled: Boolean) {
        editText.isEnabled = enabled
        ivDropdown.isEnabled = enabled
    }

    fun setDropdownIcon(@DrawableRes icon: Int) {
        ivDropdown.setImageDrawable(AppCompatResources.getDrawable(context, icon))
    }

    fun setDropdownIconColor(color: Int) {
        ivDropdown.setColorFilter(color)
    }

    fun setItems(items: Array<String>) {
        this.items = items
    }

    fun setSelectedPosition(position: Int) {
        adapter?.setSelectedPosition(position)
        itemChangedListener?.invoke(position)
    }

    fun setDropdownClickListener(listener: () -> Unit) {
        this.listener = listener
    }

    private fun setDropdownClickListener() {
        editText.setOnClickListener {
            view.performClick()
        }

        ivDropdown.setOnClickListener {
            view.performClick()
        }

        view.setOnClickListener {
            showPopupWindow()

            listener?.invoke()
        }
    }

    private fun showPopupWindow() {
        if (adapter == null) {
            adapter = TabooDropdownAdapter(
                context = context,
                layout = R.layout.taboo_dropdown_list_item,
                items = items
            ).apply {
                setDropDownViewResource(R.layout.taboo_dropdown_list_item)
            }
        }

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
                    adapter?.setSelectedPosition(position)
                    editText.setText(items[position])

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