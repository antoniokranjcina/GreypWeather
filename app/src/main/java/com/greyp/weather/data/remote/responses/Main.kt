package com.greyp.weather.data.remote.responses

import com.google.gson.annotations.SerializedName

data class Main(
    @SerializedName(value = "temp")
    val temperature: Double,
    @SerializedName(value = "feels_like")
    val feelsLike: Double,
    @SerializedName(value = "temp_min")
    val temperatureMin: Double,
    @SerializedName(value = "temp_max")
    val temperatureMax: Double,
    @SerializedName(value = "pressure")
    val pressure: Int,
    @SerializedName(value = "humidity")
    val humidity: Int,
    @SerializedName(value = "sea_level")
    val seaLevelPressure: Int,
    @SerializedName(value = "grnd_level")
    val groundLevelPressure: Int
)
