package com.kwon.taboosample

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kwon.taboo.button.TabooButton
import com.kwon.taboo.dialog.TabooAlert
import com.kwon.taboo.dialog.TabooConfirm
import com.kwon.taboo.uicore.dialog.TabooBottomAlert

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
            TabooAlert(context = this).apply {
                setTitle("알림!!")
                setMessage("알림 내용입니다~~")
                setButtonText("확인")
            }.show()
        }

        findViewById<TabooButton>(R.id.btn_confirm).setOnClickListener {
            TabooConfirm(context = this).apply {
                setTitle("경고!!")
                setMessage("저장하시겠습니까?")
                setNegativeText("취소")
                setPositiveText("저장")
            }.show()
        }

        findViewById<TabooButton>(R.id.btn_bottom_dialog).setOnClickListener {
            TabooBottomAlert(context = this).apply {
                val dialogView = LayoutInflater.from(context).inflate(com.kwon.taboo.R.layout.taboo_alert, null)
                this.setView(dialogView)
            }.show()
        }
    }
}