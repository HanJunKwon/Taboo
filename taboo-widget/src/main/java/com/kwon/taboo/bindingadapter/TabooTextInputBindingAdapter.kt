package com.kwon.taboo.bindingadapter

import android.text.Editable
import android.text.TextWatcher
import androidx.databinding.*
import com.kwon.taboo.R
import com.kwon.taboo.textfield.TabooTextInput

object TabooTextInputBindingAdapter {
    @JvmStatic
    @BindingAdapter("android:text")
    fun setAndroidText(v: TabooTextInput, value: String?) {
        if (value != v.text) v.text = value ?: ""
    }

    @JvmStatic
    @InverseBindingAdapter(attribute = "android:text", event = "android:textAttrChanged")
    fun getAndroidText(v: TabooTextInput): String = v.text

    @JvmStatic
    @BindingAdapter("android:textAttrChanged")
    fun setAndroidTextAttrChanged(v: TabooTextInput, listener: InverseBindingListener?) {
        val key = R.id.tag_content_watcher
        (v.getTag(key) as? TextWatcher)?.let { v.removeWatcher(it) }

        if (listener == null) { v.setTag(key, null); return }

        val w = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, st: Int, c: Int, a: Int) {}
            override fun onTextChanged(s: CharSequence?, st: Int, b: Int, c: Int) { listener.onChange() }
            override fun afterTextChanged(s: Editable?) {}
        }
        v.addWatcher(w)
        v.setTag(key, w)
    }
}