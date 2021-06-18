package com.greyp.weather.data.remote.responses

import com.google.gson.annotations.SerializedName

data class Coordinates(
    @SerializedName(value = "lon")
    val longitude: Double,
    @SerializedName(value = "lat")
    val latitude: Double
)
