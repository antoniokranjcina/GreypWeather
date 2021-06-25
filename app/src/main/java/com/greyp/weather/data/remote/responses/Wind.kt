package com.greyp.weather.data.remote.responses

import com.google.gson.annotations.SerializedName

data class Wind(
    @SerializedName(value = "speed")
    val speed: Double,
    @SerializedName(value = "deg")
    val directionDegrees: Double,
    @SerializedName(value = "gust")
    val gust: Double
)
