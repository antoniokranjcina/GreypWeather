package com.greyp.weather.data.repository

import android.app.Application
import android.util.Log
import androidx.room.withTransaction
import com.google.gson.Gson
import com.greyp.weather.R
import com.greyp.weather.data.local.GreypWeatherDatabase
import com.greyp.weather.data.remote.OpenWeatherMapApi
import com.greyp.weather.utils.Resource
import com.greyp.weather.utils.openWeatherToCityWeatherEntity
import com.greyp.weather.utils.openWeatherToLocationWeatherEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.UnknownHostException
import javax.inject.Inject

class Repository @Inject constructor(
    private val api: OpenWeatherMapApi,
    private val greypWeatherDatabase: GreypWeatherDatabase,
    private val application: Application
) {

    suspend fun getWeatherByCityName(cityName: String) = withContext(Dispatchers.IO) {
        try {
            val response = api.getWeatherByCityName(cityName = cityName)

            when (response.code()) {
                200 -> {
                    Log.d(TAG, "getWeatherByLocation: 200 - ${response.body()}")
                    greypWeatherDatabase.withTransaction {
                        greypWeatherDatabase.cityWeatherDao().apply {
                            deleteAllWeather()
                            response.body()?.let { insertWeather(it.openWeatherToCityWeatherEntity()) }
                        }
                    }

                    val resultToDisplay = greypWeatherDatabase.cityWeatherDao().getWeather()
                    Resource.success(resultToDisplay)
                }
                404 -> {
                    Log.e(TAG, "getWeatherByLocation: 404 - City Not Found")
                    Resource.error(errorText = "City Not Found", data = null)
                }
                else -> {
                    val error = gson.fromJson(response.errorBody()?.charStream().toString(), String::class.java)
                    Log.d(TAG, "getWeatherByLocation: else - error - $error")
                    Resource.error(errorText = error, data = null)
                }
            }
        } catch (e: UnknownHostException) {
            val error = application.getString(R.string.unknown_host)
            Log.d(TAG, "getWeatherByCityName: UnknownHostException - $error")

            val cachedData = greypWeatherDatabase.cityWeatherDao().getWeather()
            Resource.error(errorText = error, cachedData)
        } catch (e: Exception) {
            val error = e.message ?: application.getString(R.string.unexpected_error)
            Log.e(TAG, "getWeatherByCityName: Exception - $error")

            val cachedData = greypWeatherDatabase.cityWeatherDao().getWeather()
            Resource.error(errorText = error, data = cachedData)
        }
    }

    suspend fun getWeatherByLocation(latitude: String, longitude: String) = withContext(Dispatchers.IO) {
        try {
            val response = api.getWeatherByLocation(latitude = latitude, longitude = longitude)

            when (response.code()) {
                200 -> {
                    Log.d(TAG, "getWeatherByLocation: 200 - ${response.body()}")
                    greypWeatherDatabase.withTransaction {
                        greypWeatherDatabase.locationWeatherDao().apply {
                            deleteAllWeather()
                            response.body()?.let { insertWeather(it.openWeatherToLocationWeatherEntity()) }
                        }
                    }

                    val resultToDisplay = greypWeatherDatabase.locationWeatherDao().getWeather()
                    Resource.success(resultToDisplay)
                }
                404 -> {
                    Log.e(TAG, "getWeatherByLocation: 404 - City Not Found")
                    Resource.error(errorText = "City Not Found", data = null)
                }
                else -> {
                    val error = gson.fromJson(response.errorBody()?.charStream().toString(), String::class.java)
                    Log.d(TAG, "getWeatherByLocation: else - error - $error")
                    Resource.error(errorText = error, data = null)
                }
            }
        } catch (e: UnknownHostException) {
            val error = application.getString(R.string.unknown_host)
            Log.d(TAG, "getWeatherByLocation: UnknownHostException - $error")

            val cachedData = greypWeatherDatabase.locationWeatherDao().getWeather()
            Resource.error(errorText = error, cachedData)
        } catch (e: Exception) {
            val error = e.message ?: application.getString(R.string.unexpected_error)
            Log.e(TAG, "getWeatherByLocation: Exception - $error")

            val cachedData = greypWeatherDatabase.locationWeatherDao().getWeather()
            Resource.error(errorText = error, data = cachedData)
        }
    }


    companion object {
        private const val TAG = "Repository"

        private val gson = Gson()
    }

}