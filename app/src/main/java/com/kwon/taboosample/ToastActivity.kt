package com.kwon.taboosample

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kwon.taboo.button.TabooButton
import com.kwon.taboo.toast.TabooSlideToast
import com.kwon.taboo.uicore.toast.presenter.ToastPresenterCore

class ToastActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_toast)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<TabooButton>(R.id.btn_taboo_toast).setOnClickListener {

        }

        findViewById<TabooButton>(R.id.btn_taboo_slide_toast).setOnClickListener {
            TabooSlideToast(this)
                .makeText(
                    iconDrawable = ContextCompat.getDrawable(this@ToastActivity, R.drawable.ic_repair_response),
                    text = "고장 수리를 접수하였습니다."
                )
                .show()
        }

        findViewById<TabooButton>(R.id.btn_taboo_slide_toast_none_icon).setOnClickListener {
            TabooSlideToast(this)
                .makeText(
                    text = "고장 수리를 접수하였습니다."
                )
                .show()
        }

        findViewById<TabooButton>(R.id.btn_bottom_taboo_slide_toast).setOnClickListener {
            TabooSlideToast(this)
                .makeText(
                    iconDrawable = ContextCompat.getDrawable(this@ToastActivity, R.drawable.ic_repair_response),
                    text = "고장 수리를 접수하였습니다."
                )
                .setPosition(ToastPresenterCore.ToastPosition.BOTTOM)
                .show()
        }

        findViewById<TabooButton>(R.id.btn_bottom_taboo_slide_toast_none_icon).setOnClickListener {
            TabooSlideToast(this)
                .makeText(
                    text = "고장 수리를 접수하였습니다."
                )
                .setPosition(ToastPresenterCore.ToastPosition.BOTTOM)
                .show()
        }
    }
}