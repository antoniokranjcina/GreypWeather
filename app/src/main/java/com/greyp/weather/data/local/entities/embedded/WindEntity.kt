package com.greyp.weather.data.local.entities.embedded

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.greyp.weather.utils.Constants

@Entity(tableName = Constants.WIND_TABLE)
data class WindEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "speed")
    val speed: Double,
    @ColumnInfo(name = "direction_degrees")
    val directionDegrees: Double,
    @ColumnInfo(name = "gust")
    val gust: Double
)
