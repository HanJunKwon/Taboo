package com.kwon.taboo.dialog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import com.kwon.taboo.R
import com.kwon.taboo.button.TabooButton
import com.kwon.taboo.uicore.dialog.TabooAlertDialogCore

class TabooConfirm(context: Context): TabooAlertDialogCore<TabooConfirm>(context) {
    private var listener: TabooConfirmListener? = null

    private var positiveButtonText: String = ""
    private var negativeButtonText: String = ""

    init {
        setView(LayoutInflater.from(context).inflate(R.layout.taboo_confirm, null))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        findViewById<FrameLayout>(R.id.fl_content_view_wrapper).let {
            it.removeAllViews()

            if (customContentView == null) {
                it.addView(defaultContentView)
                findViewById<TextView>(R.id.tv_confirm_title).text = mTitle
                findViewById<TextView>(R.id.tv_confirm_message).text = mDescription
            } else {
                it.addView(customContentView)
            }
        }
    }

    override fun setupButtons() {
        findViewById<TabooButton>(R.id.btn_negative).apply {
            setText(negativeButtonText)
            setOnClickListener {
                listener?.onNegative()
                dismiss()
            }
        }

        findViewById<TabooButton>(R.id.btn_positive).apply {
            setText(positiveButtonText)
            setOnClickListener {
                listener?.onPositive()
                dismiss()
            }
        }
    }

    fun setPositiveText(text: CharSequence?) : TabooConfirm {
        positiveButtonText = (text ?: "").toString()

        return this
    }

    fun setPositiveText(textId: Int) : TabooConfirm {
        positiveButtonText = context.getString(textId)

        return this
    }

    fun setNegativeText(text: CharSequence?) : TabooConfirm {
        negativeButtonText = (text ?: "").toString()

        return this
    }

    fun setNegativeText(textId: Int) : TabooConfirm {
        negativeButtonText = context.getString(textId)

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