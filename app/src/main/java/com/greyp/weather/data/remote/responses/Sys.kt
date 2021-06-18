package com.greyp.weather.data.remote.responses

import com.google.gson.annotations.SerializedName

data class Sys(
    @SerializedName(value = "type")
    val type: Int,
    @SerializedName(value = "id")
    val id: Int,
    @SerializedName(value = "country")
    val country: String,
    @SerializedName(value = "sunrise")
    val sunrise: Long,
    @SerializedName(value = "sunset")
    val sunset: Long
)