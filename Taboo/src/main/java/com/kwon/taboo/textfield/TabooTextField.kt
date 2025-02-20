package com.kwon.taboo.textfield

import android.content.Context
import android.graphics.Typeface
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.View.OnFocusChangeListener
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import com.kwon.taboo.R
import com.kwon.taboo.enums.AffixType
import com.kwon.utils.edit.EditorUtils

open class TabooTextField(
    private val context: Context,
    parent: ConstraintLayout
) {
    protected val view: View = LayoutInflater.from(context).inflate(R.layout.taboo_edit_text, parent, true)
    protected val textFieldWrapper: ConstraintLayout = view.findViewById(R.id.wrapper)
    protected val editText: EditText = view.findViewById<EditText?>(R.id.edt_text).apply {
        setOnFocusChangeListener { v, hasFocus ->
            editTextOnFocusChangeListener?.onFocusChange(v, hasFocus)
            textFieldWrapper.isSelected = hasFocus
        }

        setOnClickListener { v ->
            editTextOnClickListener?.onClick(v)
        }
    }

    private var editTextOnFocusChangeListener: OnFocusChangeListener? = null
    private var editTextOnClickListener: OnClickListener? = null

    private var inputType = EditorInfo.TYPE_CLASS_TEXT
    private var isError = false

    open fun setEnabled(enabled: Boolean) {
        textFieldWrapper.isEnabled = enabled
        editText.isEnabled = enabled
    }

    fun setHint(hint: String) {
        editText.setHint(hint)
    }

    fun getHint(): CharSequence = editText.hint

    fun setText(text: String) {
        editText.setText(text)
    }

    fun getText(): String = editText.text.toString()

    /**
     * 텍스트 입력 필드의 inputType 설정 메소드.
     */
    fun setInputType(inputType: Int) {
        this.inputType = inputType
        editText.inputType = inputType

        // inputType이 비밀번호 타입이면 typeFace를 Default로 초기화.
        EditorUtils.apply {
            if (isPasswordInputType(inputType) || isVisiblePasswordInputType(inputType)) {
                editText.typeface = Typeface.DEFAULT
            }
        }
    }

    /**
     * 텍스트 입력 필드의 inputType 반환 메소드
     */
    fun getInputType() = editText.inputType

    /**
     * 비밀번호 표시
     */
    open fun showPassword() {
        val cursorPosition = editText.selectionStart

        editText.inputType = EditorInfo.TYPE_CLASS_TEXT
        editText.setSelection(cursorPosition)
    }

    /**
     * 비밀번호 숨김
     */
    open fun hiddenPassword() {
        val cursorPosition = editText.selectionStart

        editText.inputType = inputType
        editText.setSelection(cursorPosition)
    }

    fun isAnyPasswordInputType() = EditorUtils.isAnyPasswordInputType(inputType)

    fun isError(error: Boolean) {
        val backgroundDrawable = if (error) R.drawable.shape_taboo_edit_text_error else R.drawable.selector_taboo_edit_text
        textFieldWrapper.background = ContextCompat.getDrawable(context, backgroundDrawable)
    }

    fun isError() = isError

    fun setOnFocusChangeListener(l: OnFocusChangeListener?) {
        editTextOnFocusChangeListener = l
    }

    fun setOnClickListener(l: OnClickListener?) {
        editTextOnClickListener = l
    }

    fun setOnTextChangedListener(l: (text: CharSequence, start: Int, before: Int, count: Int) -> Unit) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                l.invoke(s ?: "", start, count, after)
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                l.invoke(s ?: "", start, before, count)
            }

            override fun afterTextChanged(s: Editable?) {
                l.invoke(s ?: "", 0, 0, 0)
            }
        })
    }

    protected fun setFocusable(focusable: Boolean) {
        editText.isFocusable = focusable
    }

    protected fun getFocusable() = editText.isFocusable

    protected fun setClickable(clickable: Boolean) {
        editText.isClickable = clickable
    }

    protected fun getClickable() = editText.isClickable

    fun bindView(view: View, affixType: AffixType) {
        val addIndex = if (affixType == AffixType.PREFIX) 0 else textFieldWrapper.childCount

        // 자식 뷰에 추가
        textFieldWrapper.addView(view, addIndex)

        ConstraintSet().apply {
            clone(textFieldWrapper)
            applyAffixConstraints(this, view, affixType)
            applyTo(textFieldWrapper)
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
        affixType: AffixType
    ) {
        when (affixType) {
            AffixType.PREFIX -> {
                constraintSet.connect(view.id, ConstraintSet.START, textFieldWrapper.id, ConstraintSet.START)
                constraintSet.connect(view.id, ConstraintSet.END, editText.id, ConstraintSet.START)
                constraintSet.connect(editText.id, ConstraintSet.START, view.id, ConstraintSet.END)
            }
            AffixType.SUFFIX -> {
                constraintSet.connect(view.id, ConstraintSet.START, editText.id, ConstraintSet.END)
                constraintSet.connect(view.id, ConstraintSet.END, textFieldWrapper.id, ConstraintSet.END)
                constraintSet.connect(editText.id, ConstraintSet.END, view.id, ConstraintSet.START)
            }
        }

        // 공통 제약 조건 추가
        constraintSet.connect(view.id, ConstraintSet.TOP, textFieldWrapper.id, ConstraintSet.TOP)
        constraintSet.connect(view.id, ConstraintSet.BOTTOM, textFieldWrapper.id, ConstraintSet.BOTTOM)
    }
}