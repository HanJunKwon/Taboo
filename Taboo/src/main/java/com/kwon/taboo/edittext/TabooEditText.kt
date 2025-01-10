package com.kwon.taboo.edittext

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.kwon.taboo.R
import com.kwon.taboo.databinding.TabooEditTextBinding

class TabooEditText(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {
    private val binding = TabooEditTextBinding.inflate(LayoutInflater.from(context), this, true)

    private var title: String = "Title"
    private var requiredIconVisible: Boolean = false

    private var prefixText: String = ""
    private var prefixTextAppearance = R.style.Taboo_TextAppearance_TabooEditText_Affix
    private var prefixTextColor: ColorStateList? = null

    private var suffixText: String = ""
    private var suffixTextAppearance = R.style.Taboo_TextAppearance_TabooEditText_Affix
    private var suffixTextColor: ColorStateList? = null

    private var hint: String = ""
    private var inputType: Int = 0
    private var text: String = ""

    init {
        val typed = context.obtainStyledAttributes(attrs, R.styleable.TabooEditText)

        val title = typed.getString(R.styleable.TabooEditText_title) ?: "Title"
        val requiredIconVisible = typed.getBoolean(R.styleable.TabooEditText_requiredIconVisible, false)

        val prefixText = typed.getString(R.styleable.TabooEditText_prefixText) ?: ""
        val prefixTextAppearance = typed.getResourceId(R.styleable.TabooEditText_prefixTextAppearance, R.style.Taboo_TextAppearance_TabooEditText_Affix)
        val prefixTextColor = typed.getColorStateList(R.styleable.TabooEditText_prefixTextColor)

        val suffixText = typed.getString(R.styleable.TabooEditText_suffixText) ?: ""
        val suffixTextAppearance = typed.getResourceId(R.styleable.TabooEditText_suffixTextAppearance, R.style.Taboo_TextAppearance_TabooEditText_Affix)
        val suffixTextColor = typed.getColorStateList(R.styleable.TabooEditText_suffixTextColor)

        val hint = typed.getString(R.styleable.TabooEditText_android_hint) ?: ""
        val inputType = typed.getInt(R.styleable.TabooEditText_android_inputType, 1)
        val text = typed.getString(R.styleable.TabooEditText_android_text) ?: ""

        typed.recycle()

        setTitle(title)
        setRequiredIconVisible(requiredIconVisible)

        setPrefixText(prefixText)
        setPrefixTextAppearance(prefixTextAppearance)
        setPrefixTextColor(prefixTextColor)

        setSuffixText(suffixText)
        setSuffixTextAppearance(suffixTextAppearance)
        setSuffixTextColor(suffixTextColor)

        setHint(hint)
        setInputType(inputType)
        setText(text)
    }

    fun setTitle(title: String) {
        if (this.title == title)
            return

        this.title = title
        updateTitle()
    }

    private fun updateTitle() {
        binding.tvEditTextTitle.visibility = if (this.title.isEmpty()) GONE else VISIBLE
        binding.tvEditTextTitle.text = this.title
    }

    fun setRequiredIconVisible(isVisible: Boolean = false) {
        this.requiredIconVisible = isVisible
        updateRequiredIconVisible()
    }

    private fun updateRequiredIconVisible() {
        binding.viewRequiredDot.visibility = if (requiredIconVisible) VISIBLE else GONE
    }

    fun setPrefixText(prefixText: String) {
        this.prefixText = prefixText
        updatePrefixText()
    }

    private fun updatePrefixText() {
        binding.tvPrefix.text = prefixText
        binding.tvPrefix.visibility = VISIBLE
    }

    fun setPrefixTextAppearance(prefixTextAppearance: Int) {
        if (this.prefixTextAppearance != prefixTextAppearance) {
            this.prefixTextAppearance = prefixTextAppearance
            updatePrefixTextAppearance()
        }
    }

    private fun updatePrefixTextAppearance() {
        binding.tvPrefix.setTextAppearance(this.prefixTextAppearance)
    }

    fun setPrefixTextColor(prefixTextColor: ColorStateList?) {
        if (prefixTextColor == null)
            return

        this.prefixTextColor = prefixTextColor
        updatePrefixTextColor()
    }

    private fun updatePrefixTextColor() {
        binding.tvPrefix.setTextColor(prefixTextColor)
    }

    fun setSuffixText(suffixText: String) {
        this.suffixText = suffixText
        updateSuffixText()
    }

    private fun updateSuffixText() {
        binding.tvSuffix.text = suffixText
        binding.tvSuffix.visibility = VISIBLE
    }

    fun setSuffixTextAppearance(prefixTextAppearance: Int) {
        if (this.suffixTextAppearance != prefixTextAppearance) {
            this.suffixTextAppearance = prefixTextAppearance
            updateSuffixTextAppearance()
        }
    }

    private fun updateSuffixTextAppearance() {
        binding.tvSuffix.setTextAppearance(this.suffixTextAppearance)
    }

    fun setSuffixTextColor(suffixTextColor: ColorStateList?) {
        if (suffixTextColor == null)
            return

        this.suffixTextColor = suffixTextColor
        updateSuffixTextColor()
    }

    private fun updateSuffixTextColor() {
        binding.tvSuffix.setTextColor(suffixTextColor)
    }

    fun setHint(hint: String) {
        this.hint = hint
        updateHint()
    }

    private fun updateHint() {
        binding.edtText.hint = this.hint
    }

    fun setInputType(inputType: Int) {
        this.inputType = inputType
        updateInputType()
    }

    private fun updateInputType() {
        binding.edtText.inputType = this.inputType
    }

    fun getText() = binding.edtText.text.toString()

    fun setText(text: String) {
        if (this.text == text)
            return

        this.text = text
        updateText()
    }

    private fun updateText() {
        binding.edtText.setText(this.text)
    }
}