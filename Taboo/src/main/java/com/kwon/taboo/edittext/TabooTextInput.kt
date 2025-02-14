package com.kwon.taboo.edittext

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.Typeface
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListPopupWindow
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import com.kwon.taboo.R
import com.kwon.taboo.databinding.TabooEditTextBinding
import com.kwon.taboo.databinding.TabooTextInputBinding
import com.kwon.taboo.enums.AffixType

class TabooTextInput(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {
    private val binding = TabooTextInputBinding.inflate(LayoutInflater.from(context), this, true)
    private var liningView: Any? = null

    private var title: String = "Title"
    private var requiredIconVisible: Boolean = false
    private var errorMessage: String? = null
    private var error = false

    private var hint: String = ""


    init {
        val typed = context.obtainStyledAttributes(attrs, R.styleable.TabooTextInput)

        typed.getInt(R.styleable.TabooTextInput_textInputVariant, 0).let { textInputVariant ->
            when (textInputVariant) {
                0 -> initEditTextComponent(typed)
                1 -> initDropdownComponent(typed)
            }
        }

        val enabled = typed.getBoolean(R.styleable.TabooTextInput_android_enabled, true)

        typed.recycle()

        setEnabled(enabled)
    }

    /**
     * 공통 컴포넌트 초기화
     * - Title
     * - Required Icon
     * - Error Message
     * - Error State
     *
     * [hint]가 공용 컴포넌트에서 제외된 이유는 [EditText]의 속성으로 취급되기 떄문에 [initEditTextComponent] 또는 [initDropdownComponent]를 호출하여 각각 초기화 해준다.
     */
    private fun initCommonComponent(typed: TypedArray) {
        val title = typed.getString(R.styleable.TabooTextInput_title) ?: "Title"
        val requiredIconVisible = typed.getBoolean(R.styleable.TabooTextInput_requiredIconVisible, false)
        val errorMessage = typed.getString(R.styleable.TabooTextInput_errorMessage)
        val error = typed.getBoolean(R.styleable.TabooTextInput_error, false)

        setTitle(title)
        setRequiredIconVisible(requiredIconVisible)
        setErrorMassage(errorMessage)
        setError(error)
    }

    private fun initEditTextComponent(typed: TypedArray) {
        // 공통 컴포넌트 초기화
        initCommonComponent(typed)

        // EditText 속성
        val text = typed.getString(R.styleable.TabooTextInput_android_text) ?: ""
        val hint = typed.getString(R.styleable.TabooTextInput_android_hint) ?: ""
        val inputType = typed.getInt(R.styleable.TabooTextInput_android_inputType, EditorInfo.TYPE_CLASS_TEXT)

        // Prefix 속성
        val prefixText = typed.getString(R.styleable.TabooTextInput_prefixText) ?: ""
        val prefixTextAppearance = typed.getResourceId(R.styleable.TabooTextInput_prefixTextAppearance, R.style.Taboo_TextAppearance_TabooEditText_Affix)
        val prefixTextColor = typed.getColorStateList(R.styleable.TabooTextInput_prefixTextColor)

        // Suffix 속성
        val suffixText = typed.getString(R.styleable.TabooTextInput_suffixText) ?: ""
        val suffixTextAppearance = typed.getResourceId(R.styleable.TabooTextInput_suffixTextAppearance, R.style.Taboo_TextAppearance_TabooEditText_Affix)
        val suffixTextColor = typed.getColorStateList(R.styleable.TabooTextInput_suffixTextColor)

        val passwordToggleEnabled = typed.getBoolean(R.styleable.TabooTextInput_passwordToggleEnabled, false)

        liningView = TabooEditText(
            context,
            binding.clEditTextWrapper
        ).apply {
            setText(text)
            setHint(hint)
            setInputType(inputType)
            setPasswordToggleEnable(passwordToggleEnabled)

            setAffixText(AffixType.PREFIX, prefixText)
            setAffixTextAppearance(AffixType.PREFIX, prefixTextAppearance)
            setAffixTextColor(AffixType.PREFIX, prefixTextColor)

            setAffixText(AffixType.SUFFIX, suffixText)
            setAffixTextAppearance(AffixType.SUFFIX, suffixTextAppearance)
            setAffixTextColor(AffixType.SUFFIX, suffixTextColor)
        }
    }

    /**
     *
     */
    private fun initDropdownComponent(typed: TypedArray) {
        initCommonComponent(typed)

        // EditText 속성
        val text = typed.getString(R.styleable.TabooTextInput_android_text) ?: ""
        val hint = typed.getString(R.styleable.TabooTextInput_android_hint) ?: ""

        liningView = TabooDropdown(
            context,
            binding.clEditTextWrapper
        ).apply {
            setText(text)
            setHint(hint)

            setDropdownIcon(R.drawable.ic_round_arrow_bottom)
            setDropdownIconColor(context.getColor(R.color.taboo_gray_01))
            setItems(arrayOf("Item 1", "Item 2", "Item 3"))
        }
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

    fun setErrorMassage(errorMessage: String?) {
        this.errorMessage = errorMessage
        updateErrorMessage()
    }

    private fun updateErrorMessage() {
        binding.tvErrorMessage.text = errorMessage
    }

    fun setError(error: Boolean = false) {
        this.error = error
        updateError()
    }

    private fun updateError() {
        binding.tvErrorMessage.visibility = if (error) VISIBLE else INVISIBLE
        binding.clEditTextWrapper.setBackgroundResource(if (error) R.drawable.shape_taboo_edit_text_error else R.drawable.selector_taboo_edit_text)
    }

    fun setPasswordToggleEnable(passwordToggleEnable: Boolean) {

    }

    fun setDropdownItems(items: Array<String>) {
        (liningView as? TabooDropdown)?.setItems(items)
    }

    fun setDropdownItemSelectedListener(listener: ((parent: AdapterView<*>?, view: View?, position: Int, id: Long) -> Unit)?) {
        (liningView as? TabooDropdown)?.setOnItemSelectedListener(listener)
    }



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
//    fun setOnTextChangedListener(l: (v: TabooTextInput, text: CharSequence, start: Int, before: Int, count: Int) -> Unit) {
//        binding.edtText.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//                l.invoke(this@TabooTextInput, s ?: "", start, count, after)
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                l.invoke(this@TabooTextInput, s ?: "", start, before, count)
//            }
//
//            override fun afterTextChanged(s: Editable?) {
//                l.invoke(this@TabooTextInput, s ?: "", 0, 0, 0)
//            }
//        })
//    }
}