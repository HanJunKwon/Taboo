package com.kwon.taboosample

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kwon.taboo.button.TabooButton
import com.kwon.taboo.metrics.MetricCard
import kotlin.random.Random

class MetricsCardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_metrics_card)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<TabooButton>(R.id.btn_plus).setOnClickListener {
            findViewById<MetricCard>(R.id.metric_card).apply {
                setValue(getValue().toInt() + 100)
                setPoint(Random.nextDouble(0.0, 100.0).toFloat())
            }
        }

        findViewById<TabooButton>(R.id.btn_minus).setOnClickListener {
            findViewById<MetricCard>(R.id.metric_card).apply {
                setValue(getValue().toInt() - 100)
                setPoint(Random.nextDouble(-100.0, 0.0).toFloat())
            }
        }

        findViewById<TabooButton>(R.id.btn_zero).setOnClickListener {
            findViewById<MetricCard>(R.id.metric_card).apply {
                setPoint(0f)
            }
        }
    }
}