package com.badal.mathclock

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val root = findViewById<LinearLayout>(R.id.root)
        val title = findViewById<TextView>(R.id.titleText)
        val watermark = findViewById<TextView>(R.id.watermarkText)
        val clockView = findViewById<ClockView>(R.id.clockView)

        fun applyTheme(index: Int) {
            val theme = ThemeManager.get(index)
            root.setBackgroundColor(theme.background)
            title.setTextColor(theme.textColor)
            watermark.setTextColor(theme.glowColor)
        }

        applyTheme(ThemeManager.loadIndex(this))
        clockView.onThemeChanged = { idx -> applyTheme(idx) }
    }
}
