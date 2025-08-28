package com.hansae.taboo.core.util

import android.view.inputmethod.EditorInfo
import android.widget.EditText

object EditorUtils {
    fun isAnyPasswordInputType(editText: EditText): Boolean {
        return isPasswordInputType(editText) || isVisiblePasswordInputType(editText)
    }

    fun isAnyPasswordInputType(inputType: Int): Boolean {
        return isPasswordInputType(inputType) || isVisiblePasswordInputType(inputType)
    }

    fun isPasswordInputType(editText: EditText) : Boolean {
        return isPasswordInputType(editText.inputType)
    }

    fun isPasswordInputType(inputType: Int): Boolean {
        val variation = inputType and (EditorInfo.TYPE_MASK_CLASS or EditorInfo.TYPE_MASK_VARIATION)
        return (variation == (EditorInfo.TYPE_CLASS_TEXT or EditorInfo.TYPE_TEXT_VARIATION_PASSWORD)) ||
                (variation == (EditorInfo.TYPE_CLASS_TEXT or EditorInfo.TYPE_TEXT_VARIATION_WEB_PASSWORD)) ||
                (variation == (EditorInfo.TYPE_CLASS_NUMBER or EditorInfo.TYPE_NUMBER_VARIATION_PASSWORD))
    }

    fun isVisiblePasswordInputType(editText: EditText) : Boolean {
        return isVisiblePasswordInputType(editText.inputType)
    }

    fun isVisiblePasswordInputType(inputType: Int): Boolean {
        val variation =
            inputType and (EditorInfo.TYPE_MASK_CLASS or EditorInfo.TYPE_MASK_VARIATION)
        return (variation == (EditorInfo.TYPE_CLASS_TEXT or EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD))
    }
}