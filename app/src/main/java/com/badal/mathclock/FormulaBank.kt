package com.badal.mathclock

sealed class Expr {
    data class Plain(val text: String) : Expr()
    data class Power(val base: String, val exp: String) : Expr()
    data class Fraction(val num: String, val den: String) : Expr()
    data class Sqrt(val value: String) : Expr()
    data class Log(val base: String, val arg: String) : Expr()
    data class Comb(val letter: String, val top: String, val bottom: String) : Expr()
}

object FormulaBank {
    val pool: Map<Int, List<Expr>> = mapOf(
        1 to listOf(
            Expr.Plain("2−1"), Expr.Plain("0!"), Expr.Sqrt("1"),
            Expr.Comb("C", "1", "1"), Expr.Plain("sin90°"), Expr.Plain("cos0°"),
            Expr.Plain("tan45°"), Expr.Plain("θ=1°")
        ),
        2 to listOf(
            Expr.Plain("1+1"), Expr.Sqrt("4"), Expr.Power("2", "1"),
            Expr.Comb("C", "2", "1"), Expr.Plain("2sin90°"), Expr.Plain("⌈φ⌉"),
            Expr.Plain("α=2°"), Expr.Plain("2cos0°")
        ),
        3 to listOf(
            Expr.Fraction("9", "3"), Expr.Sqrt("9"), Expr.Plain("1+2"),
            Expr.Comb("C", "3", "1"), Expr.Plain("⌊π⌋"), Expr.Plain("⌈e⌉"),
            Expr.Plain("3tan45°"), Expr.Plain("β=3°")
        ),
        4 to listOf(
            Expr.Sqrt("16"), Expr.Power("2", "2"), Expr.Fraction("16", "4"),
            Expr.Comb("C", "4", "1"), Expr.Plain("4cos0°"), Expr.Plain("γ=4°"),
            Expr.Plain("2×2")
        ),
        5 to listOf(
            Expr.Fraction("25", "5"), Expr.Sqrt("25"), Expr.Plain("3!−1"),
            Expr.Comb("C", "5", "1"), Expr.Plain("5sin90°"), Expr.Plain("δ=5°"),
            Expr.Plain("⌊π⌋+2")
        ),
        6 to listOf(
            Expr.Plain("3!"), Expr.Fraction("36", "6"), Expr.Sqrt("36"),
            Expr.Comb("C", "6", "1"), Expr.Plain("6cos0°"), Expr.Plain("2×3"),
            Expr.Plain("λ=6nm")
        ),
        7 to listOf(
            Expr.Plain("3+4"), Expr.Fraction("49", "7"), Expr.Sqrt("49"),
            Expr.Comb("C", "7", "1"), Expr.Plain("7tan45°"), Expr.Plain("⌈2π⌉"),
            Expr.Plain("v=7m/s")
        ),
        8 to listOf(
            Expr.Power("2", "3"), Expr.Fraction("64", "8"), Expr.Sqrt("64"),
            Expr.Comb("C", "8", "1"), Expr.Plain("8sin90°"), Expr.Plain("F=8N"),
            Expr.Plain("2×4")
        ),
        9 to listOf(
            Expr.Sqrt("81"), Expr.Power("3", "2"), Expr.Fraction("81", "9"),
            Expr.Comb("C", "9", "1"), Expr.Plain("9cos0°"), Expr.Plain("E=9J"),
            Expr.Plain("3×3")
        ),
        10 to listOf(
            Expr.Fraction("100", "10"), Expr.Sqrt("100"), Expr.Plain("2×5"),
            Expr.Comb("C", "10", "1"), Expr.Plain("10tan45°"), Expr.Plain("P=10W"),
            Expr.Plain("5+5")
        ),
        11 to listOf(
            Expr.Plain("5+6"), Expr.Fraction("121", "11"), Expr.Sqrt("121"),
            Expr.Comb("C", "11", "1"), Expr.Plain("11sin90°"), Expr.Plain("R=11Ω"),
            Expr.Plain("θ=11°")
        ),
        12 to listOf(
            Expr.Plain("3!×2"), Expr.Sqrt("144"), Expr.Fraction("144", "12"),
            Expr.Comb("C", "12", "1"), Expr.Plain("12cos0°"), Expr.Plain("T=12K"),
            Expr.Plain("6×2")
        )
    )

    fun random(number: Int): Expr = pool[number]!!.random()
}
