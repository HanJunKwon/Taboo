package com.kwon.taboo.dialog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import com.kwon.taboo.R
import com.kwon.taboo.button.TabooButton
import com.kwon.taboo.uicore.dialog.TabooAlertDialogCore

class TabooAlert(context: Context) : TabooAlertDialogCore<TabooAlert>(context) {
    private var listener: () -> Unit = {}

    private var buttonText: String = ""

    init {
        setView(LayoutInflater.from(context).inflate(R.layout.taboo_alert, null))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        findViewById<FrameLayout>(R.id.fl_content_view_wrapper).let {
            it.removeAllViews()

            if (customContentView == null) {
                it.addView(defaultContentView)
                findViewById<TextView>(R.id.tv_confirm_title).text = title
                findViewById<TextView>(R.id.tv_confirm_message).text = message
            } else {
                it.addView(customContentView)
            }
        }
    }

    override fun setupButtons() {
        findViewById<TabooButton>(R.id.btn_alert).apply {
            setText(buttonText)
            setOnClickListener {
                listener()
                dismiss()
            }
        }
    }

    fun setButtonText(buttonTextId: Int) : TabooAlert {
        this.buttonText = context.getString(buttonTextId)
        return this
    }

    fun setButtonText(buttonText: String) : TabooAlert {
        this.buttonText = buttonText

        return this
    }

    fun setListener(listener: () -> Unit) : TabooAlert {
        this.listener = listener
        return this
    }
}
