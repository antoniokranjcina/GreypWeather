package com.greyp.weather.data.local.entities.embedded

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.greyp.weather.utils.Constants

@Entity(tableName = Constants.SNOW_TABLE)
data class SnowEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "snow_volume_in_1h")
    val volumeIn1h: Int,
    @ColumnInfo(name = "snow_volume_in_3h")
    val volumeIn3h: Int
)