package com.greyp.weather.data.remote.responses

import com.google.gson.annotations.SerializedName

data class Snow(
    @SerializedName(value = "1h")
    val volumeIn1h: Double,
    @SerializedName(value = "3h")
    val volumeIn3h: Double
)