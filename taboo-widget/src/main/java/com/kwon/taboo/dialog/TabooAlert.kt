package com.kwon.taboo.dialog

import android.app.Dialog
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.appcompat.app.AlertDialog
import com.kwon.taboo.R
import com.kwon.taboo.button.TabooButton
import com.kwon.taboo.uicore.dialog.TabooDialogCore

class TabooAlert : TabooDialogCore<TabooAlert>() {
    private var listener: () -> Unit = {}

    private var buttonText: String = ""

    @ColorRes
    private var buttonColor: Int? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = layoutInflater.inflate(R.layout.taboo_alert, null)
        val contentView = if (customView == null) {
            layoutInflater.inflate(defaultContentView, null).apply {
                findViewById<TextView>(R.id.tv_confirm_title).text = mTitle
                findViewById<TextView>(R.id.tv_confirm_message).text = mDescription
            }
        } else {
            layoutInflater.inflate(customView!!, null)
        }

        view.findViewById<FrameLayout>(R.id.fl_content_view_wrapper).addView(contentView)

        // 버튼
        view.findViewById<TabooButton>(R.id.btn_alert).apply {
            setText(buttonText)
            buttonColor?.let {
                setButtonColorRes(it)
            }

            setOnClickListener {
                listener()
                dismiss()
            }
        }

        return AlertDialog.Builder(requireContext())
            .setView(view)
            .create()
    }

    fun setButtonText(buttonTextId: Int) : TabooAlert {
        this.buttonText = getString(buttonTextId)
        return this
    }

    fun setButtonText(buttonText: String) : TabooAlert {
        this.buttonText = buttonText

        return this
    }

    fun setButtonColorRes(@ColorRes colorResId: Int): TabooAlert {
        this.buttonColor = colorResId
        return this
    }

    fun setListener(listener: () -> Unit) : TabooAlert {
        this.listener = listener
        return this
    }
}
