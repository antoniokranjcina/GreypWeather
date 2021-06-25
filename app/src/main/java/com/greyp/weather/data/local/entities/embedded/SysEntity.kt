package com.greyp.weather.data.local.entities.embedded

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.greyp.weather.utils.Constants

@Entity(tableName = Constants.SYS_TABLE)
data class SysEntity(
    @ColumnInfo(name = "type")
    val type: Int,
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "sys_id")
    val id: Int,
    @ColumnInfo(name = "country")
    val country: String,
    @ColumnInfo(name = "sunrise")
    val sunrise: Long,
    @ColumnInfo(name = "sunset")
    val sunset: Long
)
