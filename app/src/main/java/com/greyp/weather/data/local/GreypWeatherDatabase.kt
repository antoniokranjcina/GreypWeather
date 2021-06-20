package com.greyp.weather.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.greyp.weather.data.local.daos.CityWeatherDao
import com.greyp.weather.data.local.daos.LocationWeatherDao
import com.greyp.weather.data.local.entities.CityWeatherEntity
import com.greyp.weather.data.local.entities.LocationWeatherEntity
import com.greyp.weather.data.local.entities.embedded.*

@Database(
    entities = [
        CityWeatherEntity::class, LocationWeatherEntity::class, CloudsEntity::class, CoordinatesEntity::class, MainEntity::class, RainEntity::class, SnowEntity::class, SysEntity::class,
        WeatherEntity::class, WindEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class GreypWeatherDatabase : RoomDatabase() {
    abstract fun cityWeatherDao(): CityWeatherDao
    abstract fun locationWeatherDao(): LocationWeatherDao
}