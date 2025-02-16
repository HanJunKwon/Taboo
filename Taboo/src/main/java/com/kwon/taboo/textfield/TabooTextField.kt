package com.kwon.taboo.textfield

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
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
}