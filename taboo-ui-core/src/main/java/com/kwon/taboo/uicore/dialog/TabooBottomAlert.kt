package com.kwon.taboo.uicore.dialog

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.kwon.taboo.uicore.R

class TabooBottomAlert(context: Context): AlertDialog(context, R.style.Taboo_BottomDialog) {
    init {
        this.window?.setBackgroundDrawableResource(android.R.color.transparent)
        this.window?.setLayout(
            android.view.ViewGroup.LayoutParams.MATCH_PARENT,
            android.view.ViewGroup.LayoutParams.WRAP_CONTENT
        )
        this.window?.setGravity(android.view.Gravity.BOTTOM)
        this.window?.setWindowAnimations(R.style.Taboo_BottomDialog_Animation)
    }
}