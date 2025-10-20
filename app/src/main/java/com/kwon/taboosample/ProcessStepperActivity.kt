package com.kwon.taboosample

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kwon.taboo.button.TabooButton
import com.kwon.taboo.stepper.TabooProcessStepper

class ProcessStepperActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_process_stepper)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<TabooButton>(R.id.btn_prev).setOnClickListener {
            findViewById<TabooProcessStepper>(R.id.taboo_stepper).prev()
        }

        findViewById<TabooButton>(R.id.btn_next).setOnClickListener {
            findViewById<TabooProcessStepper>(R.id.taboo_stepper).next()
        }
    }
}