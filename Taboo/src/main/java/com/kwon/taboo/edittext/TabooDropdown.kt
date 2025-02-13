package com.kwon.taboo.edittext

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import com.kwon.taboo.R

class TabooDropdown(
    private val context: Context,
    parent: ConstraintLayout
) {
    private val view = LayoutInflater.from(context).inflate(R.layout.taboo_dropdown, parent, true)
    private val editText = view.findViewById<EditText>(R.id.edt_text)
    private val ivDropdown = view.findViewById<ImageView>(R.id.iv_dropdown)

    private var listener: (() -> Unit)? = null

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

    fun setDropdownClickListener(listener: () -> Unit) {
        this.listener = listener
    }

    private fun setDropdownClickListener() {
        ivDropdown.setOnClickListener {
            listener?.invoke()
            val drawable: Drawable = ivDropdown.drawable
        }
    }
}