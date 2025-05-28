package com.kwon.taboo.radio

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.kwon.taboo.R

class TabooRadioButton(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {
    private val rootView = LayoutInflater.from(context).inflate(R.layout.taboo_radio_button, this, true)
    private var isChecked = false

    init {
        val typed = context.obtainStyledAttributes(attrs, R.styleable.TabooRadioButton)
        val label = typed.getString(R.styleable.TabooRadioButton_label) ?: ""
        val isEnabled = typed.getBoolean(R.styleable.TabooRadioButton_isEnabled, true)
        val isChecked = typed.getBoolean(R.styleable.TabooRadioButton_isChecked, false)

        typed.recycle()

        setLabel(label)
        setChecked(isChecked)
        setEnabled(isEnabled)

        super.setOnClickListener {
            if (!this.isEnabled) return@setOnClickListener

            setChecked(!this.isChecked)
        }
    }

    fun setLabel(label: String) {
        rootView.findViewById<TextView>(R.id.tv_radio_label).text = label
    }

    fun getLabel()  = rootView.findViewById<TextView>(R.id.tv_radio_label).text.toString()

    private fun updateChecked() {
        rootView.findViewById<ConstraintLayout>(R.id.wrapper).background = ContextCompat.getDrawable(
            context,
            if (isChecked) R.drawable.shape_taboo_radio_button_checked else R.drawable.shape_taboo_radio_button
        )

        rootView.findViewById<TextView>(R.id.tv_radio_label).isSelected = isChecked

        rootView.findViewById<ConstraintLayout>(R.id.cl_radio_button).background = ContextCompat.getDrawable(
            context,
            if (isChecked) R.drawable.shape_taboo_radio_button_icon_checked else R.drawable.shape_taboo_radio_button_icon
        )
    }

    fun getChecked() = isChecked

    fun setChecked(isChecked: Boolean) {
        if (this.isChecked == isChecked) return

        this.isChecked = isChecked
        updateChecked()
    }

    internal fun setOnRadioButtonGroupListener(listener: (Boolean, Int) -> Unit) {
        super.setOnClickListener {
            if (!this.isEnabled) return@setOnClickListener

            setChecked(!this.isChecked)
            listener.invoke(this.isChecked, id)
        }
    }
}