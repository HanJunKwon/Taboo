package com.kwon.taboosample

import android.content.Context
import com.kwon.taboo.uicore.dialog.TabooBottomAlert

class SampleBottomDialog(context: Context): TabooBottomAlert(context) {
    init {
        val view = layoutInflater.inflate(R.layout.dialog_bottom, null)
        this.setView(view)
    }
}