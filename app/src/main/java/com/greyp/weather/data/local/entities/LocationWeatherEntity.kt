package com.greyp.weather.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.greyp.weather.data.local.entities.embedded.*
import com.greyp.weather.utils.Constants

@Entity(tableName = Constants.LOCATION_WEATHER_TABLE)
data class LocationWeatherEntity(
    @ColumnInfo(name = "updated_at")
    val updatedAt: Long = System.currentTimeMillis(),
    @Embedded
    val coordinates: CoordinatesEntity,
    @Embedded
    val weather: WeatherEntity,
    @ColumnInfo(name = "base")
    val base: String,
    @Embedded
    val main: MainEntity,
    @ColumnInfo(name = "visibility")
    val visibility: Int,
    @Embedded
    val wind: WindEntity,
    @Embedded
    val clouds: CloudsEntity,
    @ColumnInfo(name = "time_of_data_calculation")
    val timeOfDataCalculation: Double,
    @Embedded
    val sys: SysEntity,
    @ColumnInfo(name = "timezone")
    val timezone: Int,
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "city_id")
    val cityId: Long,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "cod")
    val cod: String
)