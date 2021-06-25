package com.greyp.weather.data.local.entities.embedded

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.greyp.weather.utils.Constants

@Entity(tableName = Constants.CLOUDS_TABLE)
data class CloudsEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "cloudiness")
    val cloudiness: Int
)
