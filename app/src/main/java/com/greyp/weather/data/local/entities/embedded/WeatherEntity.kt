package com.greyp.weather.data.local.entities.embedded

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.greyp.weather.utils.Constants

@Entity(tableName = Constants.WEATHER_TABLE)
data class WeatherEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "weather_id")
    val id: Long,
    @ColumnInfo(name = "main")
    val main: String,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "icon")
    val icon: String
)
