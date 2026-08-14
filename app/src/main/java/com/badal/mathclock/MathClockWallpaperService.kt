package com.badal.mathclock

import android.os.Handler
import android.os.Looper
import android.service.wallpaper.WallpaperService
import android.view.SurfaceHolder

class MathClockWallpaperService : WallpaperService() {
    override fun onCreateEngine(): Engine = ClockEngine()

    inner class ClockEngine : Engine() {
        private val handler = Handler(Looper.getMainLooper())
        private var visible = true
        private var rotationAngle = 0f
        private val exprs = HashMap<Int, Expr>().apply { for (i in 1..12) put(i, FormulaBank.random(i)) }

        private val drawRunnable = object : Runnable {
            override fun run() {
                draw()
                if (visible) handler.postDelayed(this, 50)
            }
        }

        private val swapRunnable = object : Runnable {
            override fun run() {
                val positions = (1..12).shuffled().take(6)
                for (key in positions) exprs[key] = FormulaBank.random(key)
                if (visible) handler.postDelayed(this, 5000)
            }
        }

        override fun onVisibilityChanged(v: Boolean) {
            visible = v
            if (v) {
                handler.post(drawRunnable)
                handler.post(swapRunnable)
            } else {
                handler.removeCallbacksAndMessages(null)
            }
        }

        override fun onSurfaceDestroyed(holder: SurfaceHolder) {
            super.onSurfaceDestroyed(holder)
            visible = false
            handler.removeCallbacksAndMessages(null)
        }

        private fun draw() {
            val holder = surfaceHolder
            var canvas: android.graphics.Canvas? = null
            try {
                canvas = holder.lockCanvas()
                if (canvas != null) {
                    val theme = ThemeManager.get(ThemeManager.loadIndex(applicationContext))
                    rotationAngle = (rotationAngle + 2.5f) % 360f
                    val bmp = ClockRenderer.render(canvas.width, canvas.height, theme, exprs, rotationAngle)
                    canvas.drawBitmap(bmp, 0f, 0f, null)
                }
            } finally {
                if (canvas != null) holder.unlockCanvasAndPost(canvas)
            }
        }
    }
}
