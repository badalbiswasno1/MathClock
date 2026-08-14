package com.badal.mathclock

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews

class MathClockWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (id in appWidgetIds) updateWidget(context, appWidgetManager, id)
    }

    companion object {
        fun updateWidget(context: Context, manager: AppWidgetManager, widgetId: Int) {
            val exprs = HashMap<Int, Expr>().apply { for (i in 1..12) put(i, FormulaBank.random(i)) }
            val theme = ThemeManager.get(ThemeManager.loadIndex(context))
            val size = 500
            val bmp = ClockRenderer.render(size, size, theme, exprs, 0f)

            val views = RemoteViews(context.packageName, R.layout.widget_layout)
            views.setImageViewBitmap(R.id.widgetImage, bmp)

            val intent = Intent(context, MainActivity::class.java)
            val pending = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
            views.setOnClickPendingIntent(R.id.widgetImage, pending)

            manager.updateAppWidget(widgetId, views)
        }
    }
}
