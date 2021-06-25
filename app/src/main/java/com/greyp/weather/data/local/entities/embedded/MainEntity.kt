package com.greyp.weather.data.local.entities.embedded

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.greyp.weather.utils.Constants

@Entity(tableName = Constants.MAIN_TABLE)
data class MainEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "temp")
    val temperature: Double,
    @ColumnInfo(name = "feels_like")
    val feelsLike: Double,
    @ColumnInfo(name = "temp_min")
    val temperatureMin: Double,
    @ColumnInfo(name = "temp_max")
    val temperatureMax: Double,
    @ColumnInfo(name = "pressure")
    val pressure: Int,
    @ColumnInfo(name = "humidity")
    val humidity: Int,
    @ColumnInfo(name = "sea_level_pressure")
    val seaLevelPressure: Int,
    @ColumnInfo(name = "ground_level_pressure")
    val groundLevelPressure: Int
)
