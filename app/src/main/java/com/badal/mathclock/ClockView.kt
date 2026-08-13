package com.badal.mathclock

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.View
import java.util.Calendar
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

class ClockView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    private val current = HashMap<Int, Expr>().apply {
        for (i in 1..12) put(i, FormulaBank.random(i))
    }

    private val handler = Handler(Looper.getMainLooper())

    private val tickRunnable = object : Runnable {
        override fun run() {
            invalidate()
            handler.postDelayed(this, 1000)
        }
    }

    private val formulaSwapRunnable = object : Runnable {
        override fun run() {
            val key = (1..12).random()
            current[key] = FormulaBank.random(key)
            invalidate()
            handler.postDelayed(this, 4000)
        }
    }

    private val yellow = Paint().apply { color = Color.parseColor("#E8E040"); isAntiAlias = true; textSize = 30f }
    private val white = Paint().apply { color = Color.WHITE; strokeWidth = 8f; isAntiAlias = true }
    private val red = Paint().apply { color = Color.RED; strokeWidth = 4f; isAntiAlias = true }
    private val blue = Paint().apply { color = Color.CYAN; strokeWidth = 6f; isAntiAlias = true }
    private val ring = Paint().apply { style = Paint.Style.STROKE; strokeWidth = 12f; isAntiAlias = true; color = Color.MAGENTA }
    private val tickPaint = Paint().apply { color = Color.parseColor("#2E8B8B"); strokeWidth = 3f; isAntiAlias = true }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        handler.post(tickRunnable)
        handler.post(formulaSwapRunnable)
    }

    override fun onDetachedFromWindow() {
        handler.removeCallbacksAndMessages(null)
        super.onDetachedFromWindow()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val cx = width / 2f
        val cy = height / 2f
        val radius = min(width, height) / 2.2f

        canvas.drawCircle(cx, cy, radius, ring)

        for (t in 0 until 60) {
            val a = Math.toRadians((t * 6 - 90).toDouble())
            val outer = radius - 6
            val inner = if (t % 5 == 0) radius - 24 else radius - 14
            val x1 = cx + outer * cos(a).toFloat()
            val y1 = cy + outer * sin(a).toFloat()
            val x2 = cx + inner * cos(a).toFloat()
            val y2 = cy + inner * sin(a).toFloat()
            canvas.drawLine(x1, y1, x2, y2, tickPaint)
        }

        for (i in 1..12) {
            val angle = Math.toRadians((i * 30 - 90).toDouble())
            val x = cx + (radius * 0.75f) * cos(angle).toFloat()
            val y = cy + (radius * 0.75f) * sin(angle).toFloat()
            drawExpr(canvas, current[i]!!, x, y)
        }

        val cal = Calendar.getInstance()
        drawHand(canvas, cx, cy, radius * 0.5f, (cal.get(Calendar.HOUR) * 30 + cal.get(Calendar.MINUTE) * 0.5).toFloat(), white)
        drawHand(canvas, cx, cy, radius * 0.7f, (cal.get(Calendar.MINUTE) * 6).toFloat(), blue)
        drawHand(canvas, cx, cy, radius * 0.85f, (cal.get(Calendar.SECOND) * 6).toFloat(), red)
    }

    private fun drawHand(canvas: Canvas, cx: Float, cy: Float, length: Float, angleDeg: Float, paint: Paint) {
        val angle = Math.toRadians((angleDeg - 90).toDouble())
        val ex = cx + length * cos(angle).toFloat()
        val ey = cy + length * sin(angle).toFloat()
        canvas.drawLine(cx, cy, ex, ey, paint)
    }

    private fun drawExpr(canvas: Canvas, expr: Expr, x: Float, y: Float) {
        val base = yellow
        when (expr) {
            is Expr.Plain -> canvas.drawText(expr.text, x - base.measureText(expr.text) / 2, y, base)
            is Expr.Power -> {
                canvas.drawText(expr.base, x - 20, y, base)
                val small = Paint(base).apply { textSize = base.textSize * 0.6f }
                canvas.drawText(expr.exp, x + 5, y - 15, small)
            }
            is Expr.Sqrt -> {
                val text = "√${expr.value}"
                canvas.drawText(text, x - base.measureText(text) / 2, y, base)
            }
            is Expr.Fraction -> {
                canvas.drawText(expr.num, x - base.measureText(expr.num) / 2, y - 8, base)
                canvas.drawLine(x - 20, y, x + 20, y, base)
                canvas.drawText(expr.den, x - base.measureText(expr.den) / 2, y + 25, base)
            }
            is Expr.Log -> {
                val text = "log"
                canvas.drawText(text, x - 30, y, base)
                val small = Paint(base).apply { textSize = base.textSize * 0.6f }
                canvas.drawText(expr.base, x - 5, y + 10, small)
                canvas.drawText(expr.arg, x + 15, y, base)
            }
            is Expr.Comb -> {
                val small = Paint(base).apply { textSize = base.textSize * 0.55f }
                canvas.drawText(expr.letter, x - 15, y, base)
                canvas.drawText(expr.top, x + 8, y - 15, small)
                canvas.drawText(expr.bottom, x + 8, y + 20, small)
            }
        }
    }
}
