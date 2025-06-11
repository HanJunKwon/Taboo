package com.kwon.taboosample

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kwon.taboo.button.TabooButton
import com.kwon.taboo.numbering.TabooNumberingBall

class NumberingBallsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_numbering_balls)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<TabooButton>(R.id.btn_taboo_numbering_balls_updated).setOnClickListener {
            findViewById<TabooNumberingBall>(R.id.numbering_ball_1).apply {
//                setTextColor(ContextCompat.getColorStateList(this@NumberingBallsActivity, R.color.selector_test))
                isSelected = true
            }
            findViewById<TabooNumberingBall>(R.id.numbering_ball_2).isSelected = true
            findViewById<TabooNumberingBall>(R.id.numbering_ball_3).apply {
                setBallColor(R.color.selector_test)
                isSelected = true
            }
            findViewById<TabooNumberingBall>(R.id.numbering_ball_4).apply {
                setBallColor(ContextCompat.getColorStateList(this@NumberingBallsActivity, R.color.black))
                isSelected = true
            }
        }
    }
}