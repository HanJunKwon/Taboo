package com.kwon.taboosample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import com.kwon.taboocompose.button.TabooButton

class ComposeButtonsActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TabooButtonScreen()
        }
    }
}

@Composable
fun TabooButtonScreen() {
    TabooButton()
}