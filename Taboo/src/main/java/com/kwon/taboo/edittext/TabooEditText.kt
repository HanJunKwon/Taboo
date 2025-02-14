package com.kwon.taboo.edittext

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import com.kwon.taboo.R
import com.kwon.taboo.enums.AffixType

class TabooEditText(
    private val context: Context,
    parent: ConstraintLayout
) {
    private val view = LayoutInflater.from(context).inflate(R.layout.taboo_edit_text, parent, true)
    private val editText = view.findViewById<EditText>(R.id.edt_text)
    private var prefixTextView: TextView? = null
    private var suffixTextView: TextView? = null

    private var inputType = EditorInfo.TYPE_CLASS_TEXT

    private var passwordToggleButton: ImageView? = null
    private var passwordToggleEnable = false
    private var passwordToggleIcon = R.drawable.ic_visibility_24dp
    private var passwordToggleIconActiveColor = R.color.taboo_edit_text_password_toggle_inactive
    private var isVisiblePassword = false

    fun setText(text: String) {
        editText.setText(text)
    }

    fun getText(): String {
        return editText.text.toString()
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

    private fun isAnyPasswordInputType(inputType: Int): Boolean {
        return isPasswordInputType(inputType) || isVisiblePasswordInputType(inputType)
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

    /**
     * Prefix 또는 Suffix 를 표시할 [TextView]를 생성 및 바인딩.
     */
    private fun createAffixTextView(affixType: AffixType): TextView {
        return TextView(context).apply {
            id = View.generateViewId()
            bindAffixTextView(this, affixType)
        }
    }

    /**
     * [textView]를 화면에 바인딩한다.
     * [affixType]이 [AffixType.PREFIX]이면 `0` 번째 인덱스의 자식뷰로 추가하고,
     * [affixType]이 [AffixType.SUFFIX]이면 자식 뷰의 개수를 구한 다음에 마지막 자식뷰로 추가한다.
     */
    private fun bindAffixTextView(textView: TextView, affixType: AffixType) {
        val parentView = view.findViewById<ConstraintLayout>(R.id.cl_edit_text_wrapper)
        val addIndex = if (affixType == AffixType.PREFIX) 0 else parentView.childCount

        // 자식 뷰에 추가
        parentView.addView(textView, addIndex)

        ConstraintSet().apply {
            clone(parentView)
            applyAffixConstraints(this, textView, affixType, parentView)
            applyTo(parentView)
        }
    }

    /**
     * [AffixType.PREFIX] 또는 [AffixType.SUFFIX]에 따라 [TextView]의 [ConstraintLayout] 제약 조건을 설정하는 메소드.
     *
     * @param constraintSet [ConstraintSet] 인스턴스로, 부모 레이아웃의 제약 조건을 변경하는 데 사용됨.
     * @param view 제약 조건을 적용할 대상 [TextView].
     * @param affixType 접두사(PREFIX) 또는 접미사(SUFFIX) 유형을 지정하는 Enum 값. ([AffixType.PREFIX], [AffixType.SUFFIX])
     * @param parentView 제약 조건을 적용할 부모 [ConstraintLayout].
     *
     * - [AffixType.PREFIX] 경우:
     *   1. `textView`를 부모 뷰의 START와 연결
     *   2. `textView`의 END를 `editText`의 START와 연결
     *   3. `editText`의 START를 `textView`의 END와 연결
     *
     * - [AffixType.SUFFIX] 경우:
     *   1. `textView`의 START를 `editText`의 END와 연결
     *   2. `textView`의 END를 부모 뷰의 END와 연결
     *   3. `editText`의 END를 `textView`의 START와 연결
     *
     * - 공통 설정:
     *   1. `textView`의 TOP을 부모 뷰의 TOP과 연결
     *   2. `textView`의 BOTTOM을 부모 뷰의 BOTTOM과 연결
     */
    private fun applyAffixConstraints(
        constraintSet: ConstraintSet,
        view: View,
        affixType: AffixType,
        parentView: ConstraintLayout
    ) {
        when (affixType) {
            AffixType.PREFIX -> {
                constraintSet.connect(view.id, ConstraintSet.START, parentView.id, ConstraintSet.START)
                constraintSet.connect(view.id, ConstraintSet.END, editText.id, ConstraintSet.START)
                constraintSet.connect(editText.id, ConstraintSet.START, view.id, ConstraintSet.END)
            }
            AffixType.SUFFIX -> {
                constraintSet.connect(view.id, ConstraintSet.START, editText.id, ConstraintSet.END)
                constraintSet.connect(view.id, ConstraintSet.END, parentView.id, ConstraintSet.END)
                constraintSet.connect(editText.id, ConstraintSet.END, view.id, ConstraintSet.START)
            }
        }

        // 공통 제약 조건 추가
        constraintSet.connect(view.id, ConstraintSet.TOP, parentView.id, ConstraintSet.TOP)
        constraintSet.connect(view.id, ConstraintSet.BOTTOM, parentView.id, ConstraintSet.BOTTOM)
    }

    /**
     * [AffixType.PREFIX] 또는 [AffixType.SUFFIX]에 따라 해당하는 [TextView]를 반환하는 메서드.
     *
     * 접두사를 추가할 때는 `prefixTextView == null`이면 새롭게 생성한 후 저장하여 반환함.
     *
     * 접미사를 추가할 때는 `suffixTextViw == null`이면 새롭게 생성한 후 저장하여 반환함.
     *
     * @param affixType 접두사(PREFIX) 또는 접미사(SUFFIX) 유형을 지정하는 Enum 값.
     * @return 해당 affixType에 맞는 TextView 객체.
     */
    private fun getAffixTextView(affixType: AffixType): TextView {
        return when (affixType) {
            AffixType.PREFIX -> prefixTextView ?: createAffixTextView(affixType).also { prefixTextView = it }
            AffixType.SUFFIX -> suffixTextView ?: createAffixTextView(affixType).also { suffixTextView = it }
        }
    }

    fun setAffixText(affixType: AffixType, text: String) {
        if (isAnyPasswordInputType(inputType)) return

        getAffixTextView(affixType).text = text
    }

    fun setAffixTextAppearance(affixType: AffixType, appearance: Int) {
        if (isAnyPasswordInputType(inputType)) return

        getAffixTextView(affixType).setTextAppearance(appearance)
    }

    fun setAffixTextColor(affixType: AffixType, textColorStateList: ColorStateList?) {
        if (isAnyPasswordInputType(inputType)) return

        if (textColorStateList == null)
            return

        getAffixTextView(affixType).setTextColor(textColorStateList)
    }

    fun setEnabled(enabled: Boolean) {
        editText.isEnabled = enabled
    }

    fun setPasswordToggleEnable(passwordToggleEnable: Boolean) {
        this.passwordToggleEnable = passwordToggleEnable
        updatePasswordToggleEnabled()
    }

    private fun updatePasswordToggleEnabled() {
        if (passwordToggleEnable) {
            createPasswordToggleButton()
        } else {
            removePasswordToggleButton()
        }
    }

    private fun createPasswordToggleButton() {
        val parentView = view.findViewById<ConstraintLayout>(R.id.cl_edit_text_wrapper)
        passwordToggleButton = ImageView(context).apply {
            setImageDrawable(ContextCompat.getDrawable(context, passwordToggleIcon))
            setColorFilter(ContextCompat.getColor(context, passwordToggleIconActiveColor))
            id = View.generateViewId()

            setOnClickListener {
                if (isVisiblePassword) {
                    // 비밀번호 숨김
                    hiddenPassword()
                } else {
                    // 비밀번호 표시
                    showPassword()
                }

                // 비밀번호 표시 상태값 변경
                isVisiblePassword = !isVisiblePassword
            }
        }

        // 자식 뷰에 추가
        parentView.addView(passwordToggleButton, 1)

        ConstraintSet().apply {
            clone(parentView)
            applyAffixConstraints(this, passwordToggleButton!!, AffixType.SUFFIX, parentView)
            applyTo(parentView)
        }
    }

    private fun removePasswordToggleButton() {
        passwordToggleButton?.let {
            val parentView = view.findViewById<ConstraintLayout>(R.id.cl_edit_text_wrapper)
            parentView.removeView(it)
        }
    }

    /**
     * 비밀번호 표시
     */
    fun showPassword() {
        val cursorPosition = editText.selectionStart

        editText.inputType = EditorInfo.TYPE_CLASS_TEXT
        editText.setSelection(cursorPosition)

        passwordToggleButton?.setColorFilter(ContextCompat.getColor(context, R.color.taboo_edit_text_password_toggle_active))
    }

    /**
     * 비밀번호 숨김
     */
    fun hiddenPassword() {
        val cursorPosition = editText.selectionStart

        editText.inputType = inputType
        editText.setSelection(cursorPosition)

        passwordToggleButton?.setColorFilter(ContextCompat.getColor(context, R.color.taboo_edit_text_password_toggle_inactive))
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