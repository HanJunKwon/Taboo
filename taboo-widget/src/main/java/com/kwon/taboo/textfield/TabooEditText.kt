package com.kwon.taboo.textfield

import android.content.Context
import android.content.res.ColorStateList
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.TextViewCompat
import com.kwon.taboo.R
import com.kwon.taboo.enums.AffixType

class TabooEditText(
    private val context: Context,
    parent: ConstraintLayout
) : TabooTextField(context, parent) {
    private var prefixTextView: TextView? = null
    private var suffixTextView: TextView? = null

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
        bindView(textView, affixType)
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
        if (isAnyPasswordInputType()) return

        getAffixTextView(affixType).text = text
    }

    fun setAffixTextAppearance(affixType: AffixType, appearance: Int) {
        if (isAnyPasswordInputType()) return

        TextViewCompat.setTextAppearance(
            getAffixTextView(affixType),
            appearance
        )
    }

    fun setAffixTextColor(affixType: AffixType, textColorStateList: ColorStateList?) {
        if (isAnyPasswordInputType()) return

        if (textColorStateList == null)
            return

        getAffixTextView(affixType).setTextColor(textColorStateList)
    }

    fun setPasswordToggleEnable(passwordToggleEnable: Boolean) {
        if (!isAnyPasswordInputType()) return

        this.passwordToggleEnable = passwordToggleEnable
        updatePasswordToggleEnabled()
    }

    private fun updatePasswordToggleEnabled() {
        if (passwordToggleEnable) {
            createPasswordToggleButton()

            bindView(passwordToggleButton!!, AffixType.SUFFIX)
        } else {
            removePasswordToggleButton()
        }
    }

    private fun createPasswordToggleButton() {
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
    override fun showPassword() {
        super.showPassword()

        passwordToggleButton?.setColorFilter(ContextCompat.getColor(context, R.color.taboo_edit_text_password_toggle_active))
    }

    /**
     * 비밀번호 숨김
     */
    override fun hiddenPassword() {
        super.hiddenPassword()

        passwordToggleButton?.setColorFilter(ContextCompat.getColor(context, R.color.taboo_edit_text_password_toggle_inactive))
    }
}