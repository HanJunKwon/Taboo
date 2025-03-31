package com.kwon.taboo.radio

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.kwon.taboo.R
import com.kwon.taboo.databinding.TabooRadioButtonBinding

class TabooRadioButton(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {
    private val binding = TabooRadioButtonBinding.inflate(LayoutInflater.from(context), this, true)
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

        binding.root.rootView.setOnClickListener {
            if (!this.isEnabled) return@setOnClickListener

            setChecked(!this.isChecked)
        }
    }

    fun setLabel(label: String) {
        binding.tvRadioLabel.text = label
    }

    fun getLabel()  = binding.tvRadioLabel.text.toString()

    private fun updateChecked() {
        binding.root.background = ContextCompat.getDrawable(
            context,
            if (isChecked) R.drawable.shape_taboo_radio_button_checked else R.drawable.shape_taboo_radio_button
        )

        binding.tvRadioLabel.isSelected = isChecked

        binding.clRadioButton.background = ContextCompat.getDrawable(
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

    override fun setEnabled(isEnabled: Boolean) {
        binding.root.isEnabled = isEnabled
    }

    fun getEnabled() = binding.root.isEnabled

    internal fun setOnRadioButtonGroupListener(listener: (Boolean, Int) -> Unit) {
        binding.root.rootView.setOnClickListener {
            if (!this.isEnabled) return@setOnClickListener

            setChecked(!this.isChecked)
            listener.invoke(this.isChecked, id)
        }
    }
}