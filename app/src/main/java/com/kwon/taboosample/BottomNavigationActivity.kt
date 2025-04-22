package com.kwon.taboosample

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kwon.taboo.navigation.TabooBottomNavigation

class BottomNavigationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_navigation)

        findViewById<TabooBottomNavigation>(R.id.bottom_navigation).setMenuItemChangeListener {
            // Handle menu item change
            // You can use the index to determine which menu item was selected
            // For example, you can show a Toast or update the UI accordingly
            Log.d("BottomNavigationActivity", "Selected menu item index: $it")
        }
    }
}