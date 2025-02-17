package com.kwon.taboo.textfield

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import com.kwon.taboo.R
import com.kwon.taboo.databinding.TabooTextInputBinding
import com.kwon.taboo.enums.AffixType

class TabooTextInput(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {
    private val binding = TabooTextInputBinding.inflate(LayoutInflater.from(context), this, true)
    private var liningView: TabooTextField? = null

    private var title: String = "Title"
    private var requiredIconVisible: Boolean = false
    private var errorMessage: String? = null
    private var error = false

    private var hint: String = ""

    private var enabled = true

    init {
        val typed = context.obtainStyledAttributes(attrs, R.styleable.TabooTextInput)

        typed.getInt(R.styleable.TabooTextInput_textInputVariant, 0).let { textInputVariant ->
            when (textInputVariant) {
                0 -> initEditTextComponent(typed)
                1 -> initDropdownComponent(typed)
            }
        }

        typed.recycle()
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
        val enabled = typed.getBoolean(R.styleable.TabooTextInput_android_enabled, true)

        setTitle(title)
        setRequiredIconVisible(requiredIconVisible)
        setErrorMassage(errorMessage)
        isError(error)
        setEnabled(enabled)
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
            setEnabled(enabled)

            if (prefixText.isNotBlank()) {
                setAffixText(AffixType.PREFIX, prefixText)
                setAffixTextAppearance(AffixType.PREFIX, prefixTextAppearance)
                setAffixTextColor(AffixType.PREFIX, prefixTextColor)
            }

            if (suffixText.isNotBlank()) {
                setAffixText(AffixType.SUFFIX, suffixText)
                setAffixTextAppearance(AffixType.SUFFIX, suffixTextAppearance)
                setAffixTextColor(AffixType.SUFFIX, suffixTextColor)
            }
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
            bindDropdownIcon()

            setText(text)
            setHint(hint)
            setEnabled(enabled)

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

    fun setText(text: String) {
        liningView?.setText(text)
    }

    fun getText() = liningView?.getText() ?: ""

    fun setHint(hint: String) {
        liningView?.setHint(hint)
    }

    fun getHint() = liningView?.getHint() ?: ""

    fun setErrorMassage(errorMessage: String?) {
        this.errorMessage = errorMessage
        updateErrorMessage()
    }

    private fun updateErrorMessage() {
        binding.tvErrorMessage.text = errorMessage
    }

    fun isError(error: Boolean = false) {
        this.error = error
        updateError()
    }

    fun isError() = this.error

    private fun updateError() {
        binding.tvErrorMessage.visibility = if (error) VISIBLE else INVISIBLE
        (liningView as? TabooTextField)?.isError(error)
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        this.enabled = enabled

        binding.tvEditTextTitle.isEnabled = enabled
        binding.viewRequiredDot.isEnabled = enabled
        binding.clEditTextWrapper.isEnabled = enabled

        when (liningView) {
            is TabooEditText -> (liningView as TabooEditText).setEnabled(enabled)
            is TabooDropdown -> (liningView as TabooDropdown).setEnabled(enabled)
        }
    }

    fun setPasswordToggleEnable(passwordToggleEnable: Boolean) {

    }

    fun setOnEditTextFocusChangeListener(l: OnFocusChangeListener?) {
        (liningView as? TabooEditText)?.setOnFocusChangeListener(l)
    }

    fun setOnEditTextChangedListener(l: (text: CharSequence, start: Int, before: Int, count: Int) -> Unit) {
        (liningView as? TabooEditText)?.setOnTextChangedListener(l)
    }

    fun setDropdownItems(items: Array<String>) {
        (liningView as? TabooDropdown)?.setItems(items)
    }

    fun setDropdownSelectedPosition(position: Int) {
        (liningView as? TabooDropdown)?.setSelectedPosition(position)
    }

    fun setDropdownItemSelectedListener(listener: ((parent: AdapterView<*>?, view: View?, position: Int, id: Long) -> Unit)?) {
        (liningView as? TabooDropdown)?.setOnItemSelectedListener(listener)
    }

    fun setDropdownItemChangedListener(listener: ((position: Int) -> Unit)?) {
        (liningView as? TabooDropdown)?.setOnItemChangedListener(listener)
    }
}