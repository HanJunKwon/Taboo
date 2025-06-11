package com.kwon.taboo.dialog

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.kwon.taboo.R
import com.kwon.taboo.button.TabooButtonButtonCore

class TabooAlert(context: Context) : AlertDialog(context) {
    private var listener: () -> Unit = {}

    var title: String? = null
        private set
    var message: String? = null
        private set

    private var buttonText: String = ""

    init {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.taboo_alert, null)
        this.setView(dialogView)

        this.window?.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.shape_taboo_confirm))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        findViewById<TextView>(R.id.tv_confirm_title).text = title
        findViewById<TextView>(R.id.tv_confirm_message).text = message

        findViewById<TabooButtonButtonCore>(R.id.btn_alert).apply {
            setText(buttonText)
            setOnClickListener {
                listener()
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

    fun setButtonText(buttonText: CharSequence?) : TabooAlert {
        this.buttonText = (buttonText ?: "").toString()
        return this
    }

    fun setButtonText(buttonTextId: Int) : TabooAlert {
        this.buttonText = context.getString(buttonTextId)
        return this
    }

    fun setListener(listener: () -> Unit) : TabooAlert {
        this.listener = listener
        return this
    }
}
