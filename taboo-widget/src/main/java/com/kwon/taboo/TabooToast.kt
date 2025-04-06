package com.kwon.taboo

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.kwon.taboo.databinding.TabooToastBinding

class TabooToast(private val context: Context?) : Toast(context) {
    private val binding = TabooToastBinding.inflate(LayoutInflater.from(context), null, false)

    init {
        this.view = binding.root
        this.duration = LENGTH_SHORT // Toast 표시 시간
    }

    fun makeText(context: Context?, icon: Drawable?, text: CharSequence, duration: Int): TabooToast {
        val toast = TabooToast(context)
        toast.setIcon(icon)
        toast.setText(text)
        toast.duration = duration
        return toast
    }

    fun makeText(icon: Drawable?, text: CharSequence, duration: Int): TabooToast {
        val toast = TabooToast(context)
        toast.setIcon(icon)
        toast.setText(text)
        toast.duration = duration
        return toast
    }

    fun makeText(icon: Int, text: CharSequence, duration: Int): TabooToast {
        val toast = TabooToast(context)
        toast.setIcon(ContextCompat.getDrawable(context!!, icon))
        toast.setText(text)
        toast.duration = duration
        return toast
    }

    fun setIcon(icon: Drawable?) {
        binding.ivToastIcon.setImageDrawable(icon)
    }

    override fun setText(resId: Int) {
        binding.tvToastMessage.setText(resId)
    }

    override fun setText(text: CharSequence) {
        binding.tvToastMessage.text = text
    }
}