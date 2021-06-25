package com.greyp.weather.data.remote.responses

import com.google.gson.annotations.SerializedName

data class Clouds(
    @SerializedName(value = "all")
    val cloudiness: Int
)
