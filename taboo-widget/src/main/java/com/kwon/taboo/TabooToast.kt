package com.kwon.taboo

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat

class TabooToast(private val context: Context?) : Toast(context) {
    private val rootView = LayoutInflater.from(context).inflate(R.layout.taboo_toast, null)

    init {
        this.view = rootView
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
        rootView.findViewById<ImageView>(R.id.iv_toast_icon).setImageDrawable(icon)
    }

    override fun setText(resId: Int) {
        rootView.findViewById<TextView>(R.id.tv_toast_message).setText(resId)
    }

    override fun setText(text: CharSequence) {
        rootView.findViewById<TextView>(R.id.tv_toast_message).text = text
    }
}