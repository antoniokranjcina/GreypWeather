package com.greyp.weather.data.local.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.greyp.weather.data.local.entities.CityWeatherEntity

@Dao
interface CityWeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(cityWeather: CityWeatherEntity)

    @Query("SELECT * FROM city_weather_table LIMIT 1")
    fun getWeather(): CityWeatherEntity

    @Query("DELETE FROM city_weather_table")
    suspend fun deleteAllWeather()

}