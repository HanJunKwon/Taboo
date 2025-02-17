package com.kwon.taboo.textfield

import android.content.Context
import android.graphics.Typeface
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View.OnFocusChangeListener
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import com.kwon.taboo.R
import com.kwon.utils.edit.EditorUtils

open class TabooTextField(
    context: Context,
    parent: ConstraintLayout
) {
    protected val view = LayoutInflater.from(context).inflate(R.layout.taboo_edit_text, parent, true)
    protected val editText = view.findViewById<EditText>(R.id.edt_text)

    private var inputType = EditorInfo.TYPE_CLASS_TEXT

    open fun setEnabled(enabled: Boolean) {
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

    fun setOnFocusChangeListener(l: OnFocusChangeListener?) {
        editText.setOnFocusChangeListener { v, hasFocus ->
            l?.onFocusChange(v, hasFocus)
        }
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
}