package com.greyp.weather.data.remote.responses

import com.google.gson.annotations.SerializedName

data class OpenWeather(
    @SerializedName(value = "coord")
    val coordinates: Coordinates,
    @SerializedName(value = "weather")
    val weatherList: List<Weather>,
    @SerializedName(value = "base")
    val base: String,
    @SerializedName(value = "main")
    val main: Main,
    @SerializedName(value = "visibility")
    val visibility: Int,
    @SerializedName(value = "wind")
    val wind: Wind,
    @SerializedName(value = "clouds")
    val clouds: Clouds,
    @SerializedName(value = "rain")
    val rain: Rain,
    @SerializedName(value = "snow")
    val snow: Snow,
    @SerializedName(value = "dt")
    val timeOfDataCalculation: Double,
    @SerializedName(value = "sys")
    val sys: Sys,
    @SerializedName(value = "timezone")
    val timezone: Int,
    @SerializedName(value = "id")
    val cityId: Long,
    @SerializedName(value = "name")
    val name: String,
    @SerializedName(value = "cod")
    val cod: String
)
