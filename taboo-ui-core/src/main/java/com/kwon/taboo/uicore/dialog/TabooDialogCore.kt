package com.kwon.taboo.uicore.dialog

import android.view.LayoutInflater
import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.kwon.taboo.uicore.R
import com.kwon.taboo.uicore.util.WindowUtil

abstract class TabooDialogCore<T: TabooDialogCore<T>>: DialogFragment() {
    protected var mTitle: CharSequence = ""
    protected var mDescription: CharSequence = ""

    protected val defaultContentView: Int = R.layout.taboo_alert_dialog_base

    protected var customContentView: View? = null

    private var screenMode = WindowUtil.NORMAL_SCREEN

    override fun onStart() {
        super.onStart()

        dialog?.window?.let { window ->
            WindowUtil.applyWindowScreenMode(window, screenMode)

            window.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.shape_taboo_confirm))
        }
    }

    fun setTitle(title: String): T {
        this.mTitle = title

        return this as T
    }

    fun setTitle(title: CharSequence): T {
        this.mTitle = title

        return this as T
    }

    fun setDescription(description: String): T {
        this.mDescription = description

        return this as T
    }

    fun setDescription(description: CharSequence): T {
        this.mDescription = description

        return this as T
    }

    fun setScreenMode(@WindowUtil.ScreenMode screenMode: Int): T {
        this.screenMode = screenMode

        return this as T
    }

    /**
     * 다이얼로그 하단의 버튼을 제외한 내용이 표시될 영역에 보여줄 뷰.
     */
    fun setCustomViewResId(@LayoutRes layoutResInt: Int): T {
        setCustomView(LayoutInflater.from(requireContext()).inflate(layoutResInt, null))

        return this as T
    }

    fun setCustomView(view: View): T {
        if (customContentView != view) {
            customContentView = view
        }

        return this as T
    }
}