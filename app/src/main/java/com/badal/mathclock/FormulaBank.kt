package com.badal.mathclock

sealed class Expr {
    data class Plain(val text: String) : Expr()
    data class Power(val base: String, val exp: String) : Expr()
    data class Fraction(val num: String, val den: String) : Expr()
    data class Sqrt(val value: String) : Expr()
    data class Log(val base: String, val arg: String) : Expr()
}

object FormulaBank {
    val pool: Map<Int, List<Expr>> = mapOf(
        1 to listOf(Expr.Plain("2−1"), Expr.Log("2", "2"), Expr.Sqrt("1"), Expr.Plain("5%4")),
        2 to listOf(Expr.Plain("1+1"), Expr.Sqrt("4"), Expr.Power("2", "1"), Expr.Log("2", "4")),
        3 to listOf(Expr.Fraction("9", "3"), Expr.Sqrt("9"), Expr.Log("2", "8"), Expr.Plain("6÷2")),
        4 to listOf(Expr.Sqrt("16"), Expr.Power("2", "2"), Expr.Fraction("16", "4"), Expr.Log("2", "16")),
        5 to listOf(Expr.Fraction("25", "5"), Expr.Sqrt("25"), Expr.Plain("3!−1"), Expr.Plain("10÷2")),
        6 to listOf(Expr.Plain("3!"), Expr.Fraction("36", "6"), Expr.Sqrt("36"), Expr.Power("6", "1")),
        7 to listOf(Expr.Plain("3+4"), Expr.Fraction("49", "7"), Expr.Sqrt("49"), Expr.Plain("2³−1")),
        8 to listOf(Expr.Power("2", "3"), Expr.Fraction("64", "8"), Expr.Sqrt("64"), Expr.Plain("4!÷3")),
        9 to listOf(Expr.Sqrt("81"), Expr.Power("3", "2"), Expr.Fraction("81", "9"), Expr.Plain("3²")),
        10 to listOf(Expr.Fraction("100", "10"), Expr.Sqrt("100"), Expr.Plain("2×5"), Expr.Log("2", "1024")),
        11 to listOf(Expr.Plain("5+6"), Expr.Fraction("121", "11"), Expr.Sqrt("121"), Expr.Plain("12−1")),
        12 to listOf(Expr.Plain("3!×2"), Expr.Sqrt("144"), Expr.Fraction("144", "12"), Expr.Power("12", "1"))
    )

    fun random(number: Int): Expr = pool[number]!!.random()
}
