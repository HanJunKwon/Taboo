package com.kwon.taboo.dialog

import android.app.Dialog
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import com.kwon.taboo.R
import com.kwon.taboo.button.TabooButton
import com.kwon.taboo.uicore.dialog.TabooDialogCore

class TabooConfirm: TabooDialogCore<TabooConfirm>() {
    private var listener: TabooConfirmListener? = null

    private var positiveButtonTextId: Int = 0
    private var positiveButtonText: String = ""
    private var negativeButtonTextId: Int = 0
    private var negativeButtonText: String = ""

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = layoutInflater.inflate(R.layout.taboo_confirm, null)
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
        view.findViewById<TabooButton>(R.id.btn_positive).apply {
            setText(
                when {
                    positiveButtonTextId != 0 -> getString(positiveButtonTextId)
                    else -> positiveButtonText
                }
            )
            setOnClickListener {
                listener?.onPositive()
                dismiss()
            }
        }

        view.findViewById<TabooButton>(R.id.btn_negative).apply {
            setText(
                when {
                    negativeButtonTextId != 0 -> getString(negativeButtonTextId)
                    else -> negativeButtonText
                }
            )
            setOnClickListener {
                listener?.onNegative()
                dismiss()
            }
        }

        return AlertDialog.Builder(requireContext())
            .setView(view)
            .create()
    }

    fun setPositiveText(text: CharSequence?) : TabooConfirm {
        positiveButtonText = (text ?: "").toString()

        return this
    }

    fun setPositiveText(@StringRes textId: Int) : TabooConfirm {
        positiveButtonTextId = textId

        return this
    }

    fun setNegativeText(text: CharSequence?) : TabooConfirm {
        negativeButtonText = (text ?: "").toString()

        return this
    }

    fun setNegativeText(@StringRes textId: Int) : TabooConfirm {
        negativeButtonTextId = textId

        return this
    }

    fun setListener(listener: TabooConfirmListener?) : TabooConfirm {
        this.listener = listener
        return this
    }

    interface TabooConfirmListener {
        fun onPositive()
        fun onNegative()
    }
}