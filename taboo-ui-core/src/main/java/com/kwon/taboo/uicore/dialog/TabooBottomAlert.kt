package com.kwon.taboo.uicore.dialog

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.kwon.taboo.uicore.R
import com.kwon.taboo.uicore.util.WindowUtil
import com.kwon.taboo.uicore.util.WindowUtil.ScreenMode

open class TabooBottomAlert(context: Context): AlertDialog(context) {
    private var screenMode = WindowUtil.NORMAL_SCREEN

    init {
        this.window?.setBackgroundDrawableResource(android.R.color.transparent)
        this.window?.setLayout(
            android.view.ViewGroup.LayoutParams.MATCH_PARENT,
            android.view.ViewGroup.LayoutParams.WRAP_CONTENT
        )
        this.window?.setGravity(android.view.Gravity.BOTTOM)
        this.window?.setWindowAnimations(R.style.Taboo_BottomDialog_Animation)
    }

    @ScreenMode
    fun getScreenMode(): Int {
        return screenMode
    }

    fun setScreenMode(@ScreenMode screenMode: Int): TabooBottomAlert {
        this.screenMode = screenMode

        return this
    }

    override fun onStart() {
        super.onStart()

        WindowUtil.applyWindowScreenMode(window ?: return, screenMode)
    }
}