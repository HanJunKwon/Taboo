package com.kwon.taboo.textfield

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
import com.kwon.utils.edit.EditorUtils

class TabooEditText(
    private val context: Context,
    parent: ConstraintLayout
) : TabooTextField(context, parent) {
    private var prefixTextView: TextView? = null
    private var suffixTextView: TextView? = null

    private var inputType = EditorInfo.TYPE_CLASS_TEXT

    private var passwordToggleButton: ImageView? = null
    private var passwordToggleEnable = false
    private var passwordToggleIcon = R.drawable.ic_visibility_24dp
    private var passwordToggleIconActiveColor = R.color.taboo_edit_text_password_toggle_inactive
    private var isVisiblePassword = false

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
        if (EditorUtils.isAnyPasswordInputType(inputType)) return

        getAffixTextView(affixType).text = text
    }

    fun setAffixTextAppearance(affixType: AffixType, appearance: Int) {
        if (EditorUtils.isAnyPasswordInputType(inputType)) return

        getAffixTextView(affixType).setTextAppearance(appearance)
    }

    fun setAffixTextColor(affixType: AffixType, textColorStateList: ColorStateList?) {
        if (EditorUtils.isAnyPasswordInputType(inputType)) return

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
}