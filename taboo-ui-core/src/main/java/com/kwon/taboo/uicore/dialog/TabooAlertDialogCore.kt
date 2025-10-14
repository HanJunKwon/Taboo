package com.kwon.taboo.uicore.dialog

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.kwon.taboo.uicore.R
import com.kwon.taboo.uicore.util.WindowUtil

abstract class TabooAlertDialogCore<T: TabooAlertDialogCore<T>>(context: Context): AlertDialog(context) {
    protected var mTitle: CharSequence = ""
    protected var mDescription: CharSequence = ""

    protected val defaultContentView: View
        get() = LayoutInflater.from(context).inflate(R.layout.taboo_alert_dialog_base, null)

    protected var customContentView: View? = null

    private var screenMode = WindowUtil.NORMAL_SCREEN

    init {
        this.window?.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.shape_taboo_confirm))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupButtons()
    }

    override fun onStart() {
        super.onStart()

        window?.let {
            WindowUtil.applyWindowScreenMode(it, screenMode)
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
        setCustomView(LayoutInflater.from(context).inflate(layoutResInt, null))

        return this as T
    }

    fun setCustomView(view: View): T {
        if (customContentView != view) {
            customContentView = view
        }

        return this as T
    }

    abstract fun setupButtons()
}