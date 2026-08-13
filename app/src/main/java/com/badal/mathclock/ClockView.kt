package com.badal.mathclock

import android.content.Context
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.SweepGradient
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

    private var themeIndex: Int = ThemeManager.loadIndex(context)
    var onThemeChanged: ((Int) -> Unit)? = null

    private var rotationAngle = 0f
    private val handler = Handler(Looper.getMainLooper())

    private val animRunnable = object : Runnable {
        override fun run() {
            rotationAngle = (rotationAngle + 2.5f) % 360f
            invalidate()
            handler.postDelayed(this, 50)
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

    private val ringPaint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 14f
        isAntiAlias = true
    }
    private val tickPaint = Paint().apply { isAntiAlias = true; strokeWidth = 3f }

    init {
        isClickable = true
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        setOnClickListener {
            themeIndex = (themeIndex + 1) % ThemeManager.themes.size
            ThemeManager.saveIndex(context, themeIndex)
            onThemeChanged?.invoke(themeIndex)
            invalidate()
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        handler.post(animRunnable)
        handler.post(formulaSwapRunnable)
    }

    override fun onDetachedFromWindow() {
        handler.removeCallbacksAndMessages(null)
        super.onDetachedFromWindow()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val theme = ThemeManager.get(themeIndex)
        val cx = width / 2f
        val cy = height / 2f
        val radius = min(width, height) / 2.2f

        val textPaint = Paint().apply {
            color = theme.textColor
            isAntiAlias = true
            textSize = 40f
            setShadowLayer(14f, 0f, 0f, theme.glowColor)
        }

        val whiteHand = Paint().apply { color = theme.handHour; strokeWidth = 9f; isAntiAlias = true; setShadowLayer(8f, 0f, 0f, theme.glowColor) }
        val blueHand = Paint().apply { color = theme.handMinute; strokeWidth = 7f; isAntiAlias = true; setShadowLayer(8f, 0f, 0f, theme.glowColor) }
        val redHand = Paint().apply { color = theme.handSecond; strokeWidth = 5f; isAntiAlias = true; setShadowLayer(8f, 0f, 0f, theme.glowColor) }

        tickPaint.color = theme.glowColor

        val shader = SweepGradient(cx, cy, theme.ringColors, null)
        val matrix = Matrix()
        matrix.postRotate(rotationAngle, cx, cy)
        shader.setLocalMatrix(matrix)
        ringPaint.shader = shader
        ringPaint.setShadowLayer(24f, 0f, 0f, theme.glowColor)
        canvas.drawCircle(cx, cy, radius, ringPaint)

        for (t in 0 until 60) {
            val a = Math.toRadians((t * 6 - 90).toDouble())
            val outer = radius - 8
            val inner = if (t % 5 == 0) radius - 26 else radius - 16
            val x1 = cx + outer * cos(a).toFloat()
            val y1 = cy + outer * sin(a).toFloat()
            val x2 = cx + inner * cos(a).toFloat()
            val y2 = cy + inner * sin(a).toFloat()
            canvas.drawLine(x1, y1, x2, y2, tickPaint)
        }

        for (i in 1..12) {
            val angle = Math.toRadians((i * 30 - 90).toDouble())
            val x = cx + (radius * 0.72f) * cos(angle).toFloat()
            val y = cy + (radius * 0.72f) * sin(angle).toFloat()
            drawExpr(canvas, current[i]!!, x, y, textPaint)
        }

        val cal = Calendar.getInstance()
        drawHand(canvas, cx, cy, radius * 0.5f, (cal.get(Calendar.HOUR) * 30 + cal.get(Calendar.MINUTE) * 0.5).toFloat(), whiteHand)
        drawHand(canvas, cx, cy, radius * 0.7f, (cal.get(Calendar.MINUTE) * 6).toFloat(), blueHand)
        drawHand(canvas, cx, cy, radius * 0.85f, (cal.get(Calendar.SECOND) * 6).toFloat(), redHand)
    }

    private fun drawHand(canvas: Canvas, cx: Float, cy: Float, length: Float, angleDeg: Float, paint: Paint) {
        val angle = Math.toRadians((angleDeg - 90).toDouble())
        val ex = cx + length * cos(angle).toFloat()
        val ey = cy + length * sin(angle).toFloat()
        canvas.drawLine(cx, cy, ex, ey, paint)
    }

    private fun drawExpr(canvas: Canvas, expr: Expr, x: Float, y: Float, base: Paint) {
        when (expr) {
            is Expr.Plain -> canvas.drawText(expr.text, x - base.measureText(expr.text) / 2, y, base)
            is Expr.Power -> {
                canvas.drawText(expr.base, x - 25, y, base)
                val small = Paint(base).apply { textSize = base.textSize * 0.6f }
                canvas.drawText(expr.exp, x + 8, y - 20, small)
            }
            is Expr.Sqrt -> {
                val text = "√${expr.value}"
                canvas.drawText(text, x - base.measureText(text) / 2, y, base)
            }
            is Expr.Fraction -> {
                canvas.drawText(expr.num, x - base.measureText(expr.num) / 2, y - 10, base)
                canvas.drawLine(x - 25, y, x + 25, y, base)
                canvas.drawText(expr.den, x - base.measureText(expr.den) / 2, y + 32, base)
            }
            is Expr.Log -> {
                val small = Paint(base).apply { textSize = base.textSize * 0.6f }
                val logText = "log"
                canvas.drawText(logText, x - 50, y, base)
                val logWidth = base.measureText(logText)
                canvas.drawText(expr.base, x - 50 + logWidth, y + 12, small)
                canvas.drawText(expr.arg, x - 50 + logWidth + small.measureText(expr.base) + 6, y, base)
            }
            is Expr.Comb -> {
                val small = Paint(base).apply { textSize = base.textSize * 0.55f }
                canvas.drawText(expr.letter, x - 20, y, base)
                canvas.drawText(expr.top, x + 10, y - 20, small)
                canvas.drawText(expr.bottom, x + 10, y + 25, small)
            }
        }
    }
}
