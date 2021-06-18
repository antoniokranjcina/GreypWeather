package com.greyp.weather.data.remote

import com.greyp.weather.data.remote.responses.OpenWeather
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherMapApi {

    @GET(value = "weather")
    suspend fun getWeatherByLocation(
        @Query(value = "lat") latitude: String,
        @Query(value = "lon") longitude: String,
    ): Response<OpenWeather>

    @GET(value = "weather")
    suspend fun getWeatherByCityName(
        @Query(value = "q") cityName: String,
    ): Response<OpenWeather>

}