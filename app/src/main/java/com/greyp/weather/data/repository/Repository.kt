package com.greyp.weather.data.repository

import com.google.gson.Gson
import com.greyp.weather.data.remote.OpenWeatherMapApi
import com.greyp.weather.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class Repository @Inject constructor(private val api: OpenWeatherMapApi) {

    suspend fun getWeatherByCityName(cityName: String) = withContext(Dispatchers.IO) {
        try {
            val response = api.getWeatherByCityName(cityName = cityName)

            when (response.code()) {
                200 -> {
                    Resource.success(response.body())
                }
                404 -> {
                    Resource.error("City Not Found")
                }
                else -> {
                    val error = gson.fromJson(response.errorBody()?.charStream().toString(), String::class.java)
                    Resource.error(errorText = error)
                }
            }
        } catch (e: Exception) {
            Resource.error(errorText = e.message ?: "Unexpected error. Please try again later.")
        }
    }

    suspend fun getWeatherByLocation(latitude: String, longitude: String) = withContext(Dispatchers.IO) {
        try {
            val response = api.getWeatherByLocation(latitude = latitude, longitude = longitude)

            when (response.code()) {
                200 -> {
                    Resource.success(response.body())
                }
                404 -> {
                    Resource.error("City Not Found")
                }
                else -> {
                    val error = gson.fromJson(response.errorBody()?.charStream().toString(), String::class.java)
                    Resource.error(errorText = error)
                }
            }
        } catch (e: Exception) {
            Resource.error(errorText = e.message ?: "Unexpected error. Please try again later.")
        }
    }


    companion object {
        val gson = Gson()
    }

}