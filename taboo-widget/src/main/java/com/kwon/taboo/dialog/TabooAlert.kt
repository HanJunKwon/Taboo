package com.kwon.taboo.dialog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.kwon.taboo.R
import com.kwon.taboo.button.TabooButton
import com.kwon.taboo.uicore.attribute.ColorContainer
import com.kwon.taboo.uicore.dialog.TabooAlertDialogCore

class TabooAlert(context: Context) : TabooAlertDialogCore<TabooAlert>(context) {
    private var listener: () -> Unit = {}

    private var buttonText: String = ""

    @ColorInt
    private var buttonColor: Int? = null

    init {
        setView(LayoutInflater.from(context).inflate(R.layout.taboo_alert, null))
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
        findViewById<TabooButton>(R.id.btn_alert).apply {
            setText(buttonText)
            buttonColor?.let {
                setColorContainer(
                    ColorContainer(
                        primaryColor = it,
                        secondaryColor = it
                    )
                )
            }
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

    fun setButtonColorRes(@ColorRes colorResId: Int): TabooAlert {
        this.buttonColor = ContextCompat.getColor(context, colorResId)
        return this
    }

    fun setButtonColor(@ColorInt color: Int): TabooAlert {
        this.buttonColor = color
        return this
    }

    fun setListener(listener: () -> Unit) : TabooAlert {
        this.listener = listener
        return this
    }
}
