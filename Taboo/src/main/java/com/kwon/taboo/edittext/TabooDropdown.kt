package com.kwon.taboo.edittext

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ArrayAdapter
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

    private var listener: (() -> Unit)? = null

    private var adapter = TabooDropdownAdapter(context, R.layout.taboo_dropdown_list_item, arrayOf("바나나", "사과")).apply {
            setDropDownViewResource(R.layout.taboo_dropdown_list_item)
        }
    private var items = arrayOf<String>()

    init {
        setDropdownClickListener()
    }

    fun setText(text: String) {
        editText.setText(text)
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
        ListPopupWindow(context)
            .apply {
                setAnchorView(view)
                setModal(true)
                setPromptPosition(ListPopupWindow.POSITION_PROMPT_ABOVE)
                setAdapter(adapter)
                setOnItemClickListener({ parent, v, position, id ->
                    adapter.setSelectedPosition(position)
                    dismiss()
                })
                width = this@TabooDropdown.getWidth()
                verticalOffset = 10
            }.show()
    }

    private fun getWidth() = view.width
}