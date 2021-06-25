package com.greyp.weather.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.greyp.weather.data.repository.WeatherRepository
import javax.inject.Inject

class WorkManagerHelper @Inject constructor(
    context: Context,
    workerParameters: WorkerParameters,
    private val repository: WeatherRepository
) : CoroutineWorker(context, workerParameters) {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override suspend fun doWork(): Result {
        Log.d(TAG, "doWork: workManager started.")
        return try {
            val cityName = sharedPreferences.getString(Constants.LAST_SELECTED_CITY, Constants.NO_CITY_SAVED)
            val longitude = sharedPreferences.getString(Constants.LAST_LOCATION_LONGITUDE, Constants.NO_LOCATION_SAVED)
            val latitude = sharedPreferences.getString(Constants.LAST_LOCATION_LATITUDE, Constants.NO_LOCATION_SAVED)

            if (cityName != null && cityName != Constants.NO_CITY_SAVED) {
                repository.getWeatherByCityName(cityName = cityName)
            }
            if (longitude != null && latitude != null && longitude != Constants.NO_LOCATION_SAVED && latitude != Constants.NO_LOCATION_SAVED) {
                repository.getWeatherByLocation(latitude = latitude, longitude = longitude)
            }
            Log.d(TAG, "doWork: WorkManager result: Success.")
            Result.success()
        } catch (e: Exception) {
            Log.e(TAG, "doWork: WorkManager result: Failed, reason: ${e.localizedMessage}.")
            Result.failure()
        }
    }

    companion object {
        private const val TAG = "WorkManagerHelper"
    }

}