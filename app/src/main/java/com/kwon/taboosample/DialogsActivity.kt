package com.kwon.taboosample

import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kwon.taboo.button.TabooButton
import com.kwon.taboo.dialog.TabooAlert
import com.kwon.taboo.dialog.TabooConfirm
import com.kwon.taboo.uicore.util.WindowUtil

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
            val description = SpannableString("알림 내용입니다.!!!!")
            description.setSpan(ForegroundColorSpan(ContextCompat.getColor(this, com.kwon.taboo.uicore.R.color.taboo_red_500)), 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

            TabooAlert(context = this)
                .setTitle("알림!!")
                .setDescription(description)
                .setButtonText("확인")
                .setButtonColorRes(com.kwon.taboo.uicore.R.color.taboo_red_600)
                .show()
        }

        findViewById<TabooButton>(R.id.btn_confirm).setOnClickListener {
            TabooConfirm(context = this)
                .setTitle("경고!!")
                .setDescription("저장하시겠습니까?")
                .setScreenMode(WindowUtil.FULL_SCREEN)
                .setNegativeText(getString(R.string.button_cancel_text))
                .setPositiveText(getString(R.string.button_confirm_text))
                .show()
        }

        findViewById<TabooButton>(R.id.btn_bottom_dialog).setOnClickListener {
            SampleBottomDialog(this@DialogsActivity).show()
        }

        findViewById<TabooButton>(R.id.btn_custom_alert).setOnClickListener {

            TabooAlert(context = this)
                .setTitle("알림!!")
                .setDescription("알림 입니다!")
                .setCustomViewResId(R.layout.taboo_dialog_custom_view)
                .setButtonText("확인")
                .show()
        }

        findViewById<TabooButton>(R.id.btn_custom_dialog).setOnClickListener {
            TabooConfirm(context = this)
                .setTitle("경고!!")
                .setDescription("저장하시겠습니까?")
                .setNegativeText("취소")
                .setPositiveText("저장")
                .setCustomViewResId(R.layout.taboo_dialog_custom_view)
                .show()
        }
    }
}