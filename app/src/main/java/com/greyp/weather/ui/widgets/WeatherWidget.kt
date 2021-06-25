package com.greyp.weather.ui.widgets

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import com.greyp.weather.R
import com.greyp.weather.data.repository.WeatherRepository
import com.greyp.weather.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.roundToInt

@AndroidEntryPoint
class WeatherWidget : AppWidgetProvider() {

    private val job = SupervisorJob()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + job)

    @Inject
    lateinit var repository: WeatherRepository

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)

        val appWidgetManager = AppWidgetManager.getInstance(context)
        val man = AppWidgetManager.getInstance(context)
        val ids = man.getAppWidgetIds(context?.let { ComponentName(it, WeatherWidget::class.java) })

        coroutineScope.launch {
            repository.getLocalLocationWeather().collect { weather ->
                Log.d(TAG, "onReceive: $weather")
                ids.forEach { id ->
                    updateAppWidget(
                        context,
                        appWidgetManager,
                        id,
                        weather.name,
                        weather.weather.description,
                        weather.main.temperature.roundToInt().toString()
                    )
                }
            }
        }
    }

    override fun onDisabled(context: Context?) {
        super.onDisabled(context)
        Log.d(TAG, "onDisabled: job cancelled.")
        job.cancel()
    }

    private fun updateAppWidget(
        context: Context?, appWidgetManager: AppWidgetManager?, appWidgetId: Int, cityName: String, description: String, temperature: String
    ) {
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

        val views = RemoteViews(context?.packageName, R.layout.weather_widget)
        views.apply {
            setTextViewText(R.id.text_view_city_name, cityName)
            setTextViewText(R.id.text_view_description, description)
            setTextViewText(R.id.text_view_temperature, "$temperature ${context?.getString(R.string.degree_symbol)}")
            setOnClickPendingIntent(R.id.text_view_city_name, pendingIntent)
        }

        appWidgetManager?.updateAppWidget(appWidgetId, views)
    }

    companion object {
        private const val TAG = "WeatherWidget"
    }

}