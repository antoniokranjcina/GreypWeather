package com.greyp.weather.data.local.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.greyp.weather.data.local.entities.LocationWeatherEntity

@Dao
interface LocationWeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(locationWeather: LocationWeatherEntity)

    @Query("SELECT * FROM location_weather_table LIMIT 1")
    fun getWeather(): LocationWeatherEntity

    @Query("DELETE FROM location_weather_table")
    suspend fun deleteAllWeather()

}