package com.kwon.taboo.edittext

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.util.Log
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import com.kwon.taboo.R
import com.kwon.taboo.enums.AffixType

class TabooEditText(
    private val context: Context,
    parent: ConstraintLayout
) {
    private val view = LayoutInflater.from(context).inflate(R.layout.taboo_edit_text, parent, true)
    private val editText = view.findViewById<EditText>(R.id.edt_text)
    private val prefixTextView = view.findViewById<TextView>(R.id.tv_prefix)
    private val suffixTextView = view.findViewById<TextView>(R.id.tv_suffix)

    private var inputType = EditorInfo.TYPE_CLASS_TEXT

    fun setText(text: String) {
        editText.setText(text)
    }

    fun setHint(hint: String) {
        editText.hint = hint
    }

    fun setInputType(inputType: Int) {
        this.inputType = inputType
        editText.inputType = inputType

        // inputType이 비밀번호 타입이면 typeFace를 Default로 초기화.
        if (isPasswordInputType(inputType) || isVisiblePasswordInputType(inputType)) {
            editText.typeface = Typeface.DEFAULT
        }
    }

    private fun isPasswordInputType(inputType: Int): Boolean {
        val variation = inputType and (EditorInfo.TYPE_MASK_CLASS or EditorInfo.TYPE_MASK_VARIATION)
        return (variation == (EditorInfo.TYPE_CLASS_TEXT or EditorInfo.TYPE_TEXT_VARIATION_PASSWORD)) ||
                (variation == (EditorInfo.TYPE_CLASS_TEXT or EditorInfo.TYPE_TEXT_VARIATION_WEB_PASSWORD)) ||
                (variation == (EditorInfo.TYPE_CLASS_NUMBER or EditorInfo.TYPE_NUMBER_VARIATION_PASSWORD))
    }

    private fun isVisiblePasswordInputType(inputType: Int): Boolean {
        val variation =
            inputType and (EditorInfo.TYPE_MASK_CLASS or EditorInfo.TYPE_MASK_VARIATION)
        return (variation == (EditorInfo.TYPE_CLASS_TEXT or EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD))
    }

    private fun getAffixTextView(affixType: AffixType): TextView {
        return when (affixType) {
            AffixType.PREFIX -> prefixTextView
            AffixType.SUFFIX -> suffixTextView
        }
    }

    fun setAffixText(affixType: AffixType, text: String) {
        getAffixTextView(affixType).text = text
    }


    fun setAffixTextAppearance(affixType: AffixType, appearance: Int) {
        getAffixTextView(affixType).setTextAppearance(appearance)
    }

    fun setAffixTextColor(affixType: AffixType, textColorStateList: ColorStateList?) {
        if (textColorStateList == null)
            return

        getAffixTextView(affixType).setTextColor(textColorStateList)
    }

    fun setEnabled(enabled: Boolean) {
        editText.isEnabled = enabled
    }

    fun showPassword() {
        val cursorPosition = editText.selectionStart

        editText.inputType = EditorInfo.TYPE_CLASS_TEXT
        editText.setSelection(cursorPosition)
    }

    fun hiddenPassword() {
        val cursorPosition = editText.selectionStart

        editText.inputType = inputType
        editText.setSelection(cursorPosition)
    }




//    private val binding = TabooEditTextBinding.inflate(LayoutInflater.from(context), this, true)
//
//    private var title: String = "Title"
//    private var requiredIconVisible: Boolean = false
//    private var errorMessage: String? = null
//    private var error = false
//
//    private var prefixText: String = ""
//    private var prefixTextAppearance = R.style.Taboo_TextAppearance_TabooEditText_Affix
//    private var prefixTextColor: ColorStateList? = null
//
//    private var suffixText: String = ""
//    private var suffixTextAppearance = R.style.Taboo_TextAppearance_TabooEditText_Affix
//    private var suffixTextColor: ColorStateList? = null
//
//    private var hint: String = ""
//    private var inputType: Int = 0
//    private var text: String = ""
//    private var passwordToggleEnable = false
//
//    private var isVisiblePassword = false
//
//    init {
//        val typed = context.obtainStyledAttributes(attrs, R.styleable.TabooEditText)
//
//        val title = typed.getString(R.styleable.TabooEditText_title) ?: "Title"
//        val requiredIconVisible = typed.getBoolean(R.styleable.TabooEditText_requiredIconVisible, false)
//        val errorMessage = typed.getString(R.styleable.TabooEditText_errorMessage)
//        val error = typed.getBoolean(R.styleable.TabooEditText_error, false)
//
//        val prefixText = typed.getString(R.styleable.TabooEditText_prefixText) ?: ""
//        val prefixTextAppearance = typed.getResourceId(R.styleable.TabooEditText_prefixTextAppearance, R.style.Taboo_TextAppearance_TabooEditText_Affix)
//        val prefixTextColor = typed.getColorStateList(R.styleable.TabooEditText_prefixTextColor)
//
//        val suffixText = typed.getString(R.styleable.TabooEditText_suffixText) ?: ""
//        val suffixTextAppearance = typed.getResourceId(R.styleable.TabooEditText_suffixTextAppearance, R.style.Taboo_TextAppearance_TabooEditText_Affix)
//        val suffixTextColor = typed.getColorStateList(R.styleable.TabooEditText_suffixTextColor)
//
//        val hint = typed.getString(R.styleable.TabooEditText_android_hint) ?: ""
//        val inputType = typed.getInt(R.styleable.TabooEditText_android_inputType, 1)
//        val text = typed.getString(R.styleable.TabooEditText_android_text) ?: ""
//        val enabled = typed.getBoolean(R.styleable.TabooEditText_android_enabled, true)
//
//        val passwordToggleEnabled = typed.getBoolean(R.styleable.TabooEditText_passwordToggleEnabled, false)
//
//        typed.recycle()
//
//        setTitle(title)
//        setRequiredIconVisible(requiredIconVisible)
//        setErrorMassage(errorMessage)
//        setError(error)
//
//        setPrefixText(prefixText)
//        setPrefixTextAppearance(prefixTextAppearance)
//        setPrefixTextColor(prefixTextColor)
//
//        setSuffixText(suffixText)
//        setSuffixTextAppearance(suffixTextAppearance)
//        setSuffixTextColor(suffixTextColor)
//
//        setHint(hint)
//        setInputType(inputType)
//        setPasswordToggleEnabledInternal(passwordToggleEnabled)
//        setText(text)
//        setEnabled(enabled)
//
//        updatePasswordToggleEnabled()
//
//        val inner = LayoutInflater.from(context).inflate(R.layout.taboo_dropdown, this, false)
//        binding.clEditTextWrapper.addView(inner)
//    }
//
//    fun setTitle(title: String) {
//        if (this.title == title)
//            return
//
//        this.title = title
//        updateTitle()
//    }
//
//    private fun updateTitle() {
//        binding.tvEditTextTitle.visibility = if (this.title.isEmpty()) GONE else VISIBLE
//        binding.tvEditTextTitle.text = this.title
//    }
//
//    fun setRequiredIconVisible(isVisible: Boolean = false) {
//        this.requiredIconVisible = isVisible
//        updateRequiredIconVisible()
//    }
//
//    private fun updateRequiredIconVisible() {
//        binding.viewRequiredDot.visibility = if (requiredIconVisible) VISIBLE else GONE
//    }
//
//    fun setErrorMassage(errorMessage: String?) {
//        this.errorMessage = errorMessage
//        updateErrorMessage()
//    }
//
//    private fun updateErrorMessage() {
//        binding.tvErrorMessage.text = errorMessage
//    }
//
//    fun setError(error: Boolean = false) {
//        this.error = error
//        updateError()
//    }
//
//    private fun updateError() {
//        binding.tvErrorMessage.visibility = if (error) VISIBLE else INVISIBLE
//        binding.clEditTextWrapper.setBackgroundResource(if (error) R.drawable.shape_taboo_edit_text_error else R.drawable.selector_taboo_edit_text)
//    }
//
//    fun setPrefixText(prefixText: String) {
//        this.prefixText = prefixText
//        updatePrefixText()
//    }
//
//    private fun updatePrefixText() {
//        binding.tvPrefix.text = prefixText
//        binding.tvPrefix.visibility = VISIBLE
//    }
//
//    fun setPrefixTextAppearance(prefixTextAppearance: Int) {
//        if (this.prefixTextAppearance != prefixTextAppearance) {
//            this.prefixTextAppearance = prefixTextAppearance
//            updatePrefixTextAppearance()
//        }
//    }
//
//    private fun updatePrefixTextAppearance() {
//        binding.tvPrefix.setTextAppearance(this.prefixTextAppearance)
//    }
//
//    fun setPrefixTextColor(prefixTextColor: ColorStateList?) {
//        if (prefixTextColor == null)
//            return
//
//        this.prefixTextColor = prefixTextColor
//        updatePrefixTextColor()
//    }
//
//    private fun updatePrefixTextColor() {
//        binding.tvPrefix.setTextColor(prefixTextColor)
//    }
//
//    fun setSuffixText(suffixText: String) {
//        this.suffixText = suffixText
//        updateSuffixText()
//    }
//
//    private fun updateSuffixText() {
//        binding.tvSuffix.text = suffixText
//        binding.tvSuffix.visibility = VISIBLE
//    }
//
//    fun setSuffixTextAppearance(prefixTextAppearance: Int) {
//        if (this.suffixTextAppearance != prefixTextAppearance) {
//            this.suffixTextAppearance = prefixTextAppearance
//            updateSuffixTextAppearance()
//        }
//    }
//
//    private fun updateSuffixTextAppearance() {
//        binding.tvSuffix.setTextAppearance(this.suffixTextAppearance)
//    }
//
//    fun setSuffixTextColor(suffixTextColor: ColorStateList?) {
//        if (suffixTextColor == null)
//            return
//
//        this.suffixTextColor = suffixTextColor
//        updateSuffixTextColor()
//    }
//
//    private fun updateSuffixTextColor() {
//        binding.tvSuffix.setTextColor(suffixTextColor)
//    }
//
//    fun setHint(hint: String) {
//        this.hint = hint
//        updateHint()
//    }
//
//    private fun updateHint() {
//        binding.edtText.hint = this.hint
//    }
//
//    fun setInputType(inputType: Int) {
//        this.inputType = inputType
//        updateInputType()
//    }
//
//    private fun updateInputType() {
//        binding.edtText.inputType = inputType
//
//        // inputType이 비밀번호 타입이면 typeFace를 Default로 초기화.
//        if (isPasswordInputType(inputType) || isVisiblePasswordInputType(inputType)) {
//            binding.edtText.typeface = Typeface.DEFAULT
//        }
//    }
//
//    private fun isPasswordInputType(inputType: Int): Boolean {
//        val variation = inputType and (EditorInfo.TYPE_MASK_CLASS or EditorInfo.TYPE_MASK_VARIATION)
//        return (variation == (EditorInfo.TYPE_CLASS_TEXT or EditorInfo.TYPE_TEXT_VARIATION_PASSWORD)) ||
//                (variation == (EditorInfo.TYPE_CLASS_TEXT or EditorInfo.TYPE_TEXT_VARIATION_WEB_PASSWORD)) ||
//                (variation == (EditorInfo.TYPE_CLASS_NUMBER or EditorInfo.TYPE_NUMBER_VARIATION_PASSWORD))
//    }
//
//    private fun isVisiblePasswordInputType(inputType: Int): Boolean {
//        val variation =
//            inputType and (EditorInfo.TYPE_MASK_CLASS or EditorInfo.TYPE_MASK_VARIATION)
//        return (variation == (EditorInfo.TYPE_CLASS_TEXT or EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD))
//    }

//    fun setPasswordToggleEnabledInternal(passwordToggleEnabled: Boolean) {
//        this.passwordToggleEnable = passwordToggleEnable
//    }
//
//    fun setPasswordToggleEnabled(passwordToggleEnabled: Boolean) {
//        setPasswordToggleEnabledInternal(passwordToggleEnabled)
//        updatePasswordToggleEnabled()
//    }
//
//    private fun updatePasswordToggleEnabled() {
//        val inputType = binding.edtText.inputType
//
//        if (isPasswordInputType(inputType) || isVisiblePasswordInputType(inputType)) {
//            val icon = AppCompatResources.getDrawable(context, R.drawable.ic_visibility_24dp_e5e8eb)?.mutate()
//            binding.ivPasswordToggle.setImageDrawable(icon)
//            binding.ivPasswordToggle.setColorFilter(context.getColor(R.color.taboo_gray_01))
//            binding.ivPasswordToggle.setOnClickListener {
//                val cursorPosition = binding.edtText.selectionStart
//
//                if (isVisiblePassword) {
//                    // 비밀번호 숨김
//                    binding.edtText.inputType = inputType
//                    binding.ivPasswordToggle.setColorFilter(context.getColor(R.color.taboo_gray_01))
//                } else {
//                    // 비밀번호 표시
//                    binding.edtText.inputType = EditorInfo.TYPE_CLASS_TEXT
//                    binding.ivPasswordToggle.setColorFilter(context.getColor(R.color.white))
//                }
//
//                // 상태값 변경
//                isVisiblePassword = !isVisiblePassword
//
//                // 커서 위치 변경
//                binding.edtText.setSelection(cursorPosition)
//            }
//        } else {
//            binding.ivPasswordToggle.setImageDrawable(null)
//        }
//    }
//
//    fun getText() = binding.edtText.text.toString()
//
//    fun setText(text: String) {
//        if (this.text == text)
//            return
//
//        this.text = text
//        updateText()
//    }
//
//    private fun updateText() {
//        binding.edtText.setText(this.text)
//    }
//
//    override fun setEnabled(enabled: Boolean) {
//        super.setEnabled(enabled)
//        binding.tvEditTextTitle.isEnabled = enabled
//        binding.viewRequiredDot.isEnabled = enabled
//        binding.clEditTextWrapper.isEnabled = enabled
//        binding.edtText.isEnabled = enabled
//    }
//
//    override fun hasFocus(): Boolean {
//        return binding.edtText.hasFocus()
//    }
//
//    override fun setOnFocusChangeListener(l: OnFocusChangeListener?) {
//        binding.edtText.setOnFocusChangeListener { v, hasFocus ->
//            l?.onFocusChange(v, hasFocus)
//            super.setOnFocusChangeListener(l)
//        }
//    }
//
//    fun setOnTextChangedListener(l: (v: TabooEditText, text: CharSequence, start: Int, before: Int, count: Int) -> Unit) {
//        binding.edtText.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//                l.invoke(this@TabooEditText, s ?: "", start, count, after)
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                l.invoke(this@TabooEditText, s ?: "", start, before, count)
//            }
//
//            override fun afterTextChanged(s: Editable?) {
//                l.invoke(this@TabooEditText, s ?: "", 0, 0, 0)
//            }
//        })
//    }
}