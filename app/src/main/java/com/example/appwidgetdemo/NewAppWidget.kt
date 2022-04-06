package com.example.appwidgetdemo

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import java.text.DateFormat
import java.util.*

/**
 * Implementation of App Widget functionality.
 */
class NewAppWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    var sharedPreferencesName = "com.example.appwidgetdemo"
    var COUNT_KEY = "count"
    val widgetText = context.getString(R.string.appwidget_text)
    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.new_app_widget)
    views.setTextViewText(R.id.appwidget_id_text, appWidgetId.toString())

    var mSharedPreference = context.getSharedPreferences(sharedPreferencesName,0)
    var count = mSharedPreference.getInt(COUNT_KEY+appWidgetId,0)
    count++

    val dateString = DateFormat.getTimeInstance(DateFormat.SHORT).format(Date())

    views.setTextViewText(R.id.appwidget_update,context.resources.getString(R.string.date_count_format,count,dateString))

    val preEditor = mSharedPreference.edit()
    preEditor.putInt(COUNT_KEY+appWidgetId,count)
    preEditor.apply()

    val intentUpdate = Intent(context,NewAppWidget::class.java)
    intentUpdate.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE

    val idArray = intArrayOf(appWidgetId)
    intentUpdate.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,idArray)


    val pendingUpdate = PendingIntent.getBroadcast(context,appWidgetId,intentUpdate,PendingIntent.FLAG_UPDATE_CURRENT)

    views.setOnClickPendingIntent(R.id.btn_update,pendingUpdate)

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}