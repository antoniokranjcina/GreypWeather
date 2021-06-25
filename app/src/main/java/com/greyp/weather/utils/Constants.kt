package com.greyp.weather.utils

object Constants {

    // Retrofit
    const val API_URL = "https://api.openweathermap.org/data/2.5/"
    const val API_KEY = "8e3ab52606ebc913f4c53a3789292b4c"

    // SharedPreferences
    const val OPEN_WEATHER_PREFERENCES = "open_weather_shared_preferences"
    const val LAST_SELECTED_CITY = "last_selected_city"
    const val LAST_LOCATION_LONGITUDE = "last_location_longitude"
    const val LAST_LOCATION_LATITUDE = "last_location_latitude"
    const val NO_CITY_SAVED = "no_city_saved"
    const val NO_LOCATION_SAVED = "no_location_saved"

    // Permission request
    const val REQUEST_CHECK_CODE = 1

    // Room
    const val DATABASE_NAME = "greyp_open_weather_database"
    const val CLOUDS_TABLE = "clouds_table"
    const val COORDINATES_TABLE = "coordinates_table"
    const val MAIN_TABLE = "main_table"
    const val RAIN_TABLE = "rain_table"
    const val SNOW_TABLE = "snow_table"
    const val SYS_TABLE = "sys_table"
    const val WEATHER_TABLE = "weather_table"
    const val WIND_TABLE = "wind_table"
    const val CITY_WEATHER_TABLE = "city_weather_table"
    const val LOCATION_WEATHER_TABLE = "location_weather_table"

    // WorkManager
    const val UNIQUE_WORK_NAME = "greyp_work"

}