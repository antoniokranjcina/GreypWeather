package com.greyp.weather.data.remote.responses

import com.google.gson.annotations.SerializedName

data class Weather(
    @SerializedName(value = "id")
    val id: Long,
    @SerializedName(value = "main")
    val main: String,
    @SerializedName(value = "description")
    val description: String,
    @SerializedName(value = "icon")
    val icon: String
)
