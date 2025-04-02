package com.kwon.taboo.dialog

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.kwon.taboo.R
import com.kwon.taboo.button.TabooButton

class TabooConfirm(context: Context): AlertDialog(context) {
    private var listener: TabooConfirmListener? = null

    var title: String? = null
        private set
    var message: String? = null
        private set

    private var positiveButtonText: String = ""
    private var negativeButtonText: String = ""

    init {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.taboo_confirm, null)
        this.setView(dialogView)

        this.window?.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.shape_taboo_confirm))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        findViewById<TextView>(R.id.tv_confirm_title).text = title
        findViewById<TextView>(R.id.tv_confirm_message).text = message

        findViewById<TabooButton>(R.id.btn_negative).apply {
            text = negativeButtonText
            setOnClickListener {
                listener?.onNegative()
                dismiss()
            }
        }

        findViewById<TabooButton>(R.id.btn_positive).apply {
            text = positiveButtonText
            setOnClickListener {
                listener?.onPositive()
                dismiss()
            }
        }
    }

    override fun setTitle(title: CharSequence?) {
        this.title = (title ?: "").toString()
    }

    override fun setTitle(titleId: Int) {
        this.title = context.getString(titleId)
    }

    override fun setMessage(message: CharSequence?) {
        this.message = (message ?: "").toString()
    }

    fun setMessage(messageId: Int) {
        this.message = context.getString(messageId)
    }

    fun setPositiveText(text: CharSequence?) {
        positiveButtonText = (text ?: "").toString()
    }

    fun setPositiveText(textId: Int) {
        positiveButtonText = context.getString(textId)
    }

    fun setNegativeText(text: CharSequence?) {
        negativeButtonText = (text ?: "").toString()
    }

    fun setNegativeText(textId: Int) {
        negativeButtonText = context.getString(textId)
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