package com.kwon.taboosample

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kwon.taboo.button.TabooButton
import com.kwon.taboo.dialog.TabooAlert
import com.kwon.taboo.dialog.TabooConfirm

class DialogsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dialogs)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<TabooButton>(R.id.btn_alert).setOnClickListener {
            TabooAlert(context = this)
                .setTitle("알림!!")
                .setMessage("알림 내용입니다~~")
                .setButtonText("확인")
                .show()
        }

        findViewById<TabooButton>(R.id.btn_confirm).setOnClickListener {
            TabooConfirm(context = this)
                .setTitle("경고!!")
                .setMessage("저장하시겠습니까?")
                .setNegativeText("취소")
                .setPositiveText("저장")
                .show()
        }

        findViewById<TabooButton>(R.id.btn_bottom_dialog).setOnClickListener {
            SampleBottomDialog(this@DialogsActivity).show()
        }

        findViewById<TabooButton>(R.id.btn_custom_alert).setOnClickListener {
            TabooAlert(context = this)
                .setTitle("알림!!")
                .setMessage("알림 내용입니다~~")
                .setCustomViewResId(R.layout.taboo_dialog_custom_view)
                .setButtonText("확인")
                .show()
        }

        findViewById<TabooButton>(R.id.btn_custom_dialog).setOnClickListener {
            TabooConfirm(context = this)
                .setTitle("경고!!")
                .setMessage("저장하시겠습니까?")
                .setNegativeText("취소")
                .setPositiveText("저장")
                .setCustomViewResId(R.layout.taboo_dialog_custom_view)
                .show()
        }
    }
}