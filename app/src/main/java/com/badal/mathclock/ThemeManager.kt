package com.badal.mathclock

import android.content.Context
import android.graphics.Color

data class ClockTheme(
    val name: String,
    val background: Int,
    val textColor: Int,
    val glowColor: Int,
    val ringColors: IntArray,
    val handHour: Int,
    val handMinute: Int,
    val handSecond: Int
)

object ThemeManager {
    val themes = listOf(
        ClockTheme(
            "Neon",
            Color.parseColor("#000000"),
            Color.parseColor("#E8E040"),
            Color.parseColor("#FFF176"),
            intArrayOf(
                Color.parseColor("#FF9F1C"), Color.parseColor("#E6007A"),
                Color.parseColor("#7B2FF7"), Color.parseColor("#2B6CFF"),
                Color.parseColor("#00C2D1"), Color.parseColor("#00E5A0"),
                Color.parseColor("#FFD400"), Color.parseColor("#FF9F1C")
            ),
            Color.WHITE, Color.CYAN, Color.RED
        ),
        ClockTheme(
            "Ocean",
            Color.parseColor("#001014"),
            Color.parseColor("#7FE8E0"),
            Color.parseColor("#00FFF0"),
            intArrayOf(
                Color.parseColor("#003B4A"), Color.parseColor("#0077B6"),
                Color.parseColor("#00B4D8"), Color.parseColor("#90E0EF"),
                Color.parseColor("#00B4D8"), Color.parseColor("#0077B6"),
                Color.parseColor("#003B4A"), Color.parseColor("#003B4A")
            ),
            Color.WHITE, Color.parseColor("#90E0EF"), Color.parseColor("#FF6B6B")
        ),
        ClockTheme(
            "Sunset",
            Color.parseColor("#140A08"),
            Color.parseColor("#FFD37F"),
            Color.parseColor("#FF9E5E"),
            intArrayOf(
                Color.parseColor("#FF4E00"), Color.parseColor("#FF9100"),
                Color.parseColor("#FFC300"), Color.parseColor("#FF5DA2"),
                Color.parseColor("#C0399F"), Color.parseColor("#7B2D8E"),
                Color.parseColor("#FF4E00"), Color.parseColor("#FF4E00")
            ),
            Color.WHITE, Color.parseColor("#FFC300"), Color.parseColor("#FF3B30")
        ),
        ClockTheme(
            "Monochrome",
            Color.parseColor("#0A0A0A"),
            Color.parseColor("#E0E0E0"),
            Color.parseColor("#FFFFFF"),
            intArrayOf(
                Color.parseColor("#444444"), Color.parseColor("#888888"),
                Color.parseColor("#CCCCCC"), Color.parseColor("#888888"),
                Color.parseColor("#444444"), Color.parseColor("#222222"),
                Color.parseColor("#444444"), Color.parseColor("#444444")
            ),
            Color.WHITE, Color.parseColor("#AAAAAA"), Color.parseColor("#FF3B30")
        )
    )

    fun get(index: Int): ClockTheme = themes[index % themes.size]

    fun loadIndex(context: Context): Int {
        val prefs = context.getSharedPreferences("mathclock_prefs", Context.MODE_PRIVATE)
        return prefs.getInt("theme_index", 0)
    }

    fun saveIndex(context: Context, index: Int) {
        val prefs = context.getSharedPreferences("mathclock_prefs", Context.MODE_PRIVATE)
        prefs.edit().putInt("theme_index", index).apply()
    }
}
