package com.greyp.weather.utils

import com.greyp.weather.data.local.entities.CityWeatherEntity
import com.greyp.weather.data.local.entities.LocationWeatherEntity
import com.greyp.weather.data.local.entities.embedded.*
import com.greyp.weather.data.remote.responses.OpenWeather

fun OpenWeather.openWeatherToCityWeatherEntity(): CityWeatherEntity =
    CityWeatherEntity(
        coordinates = CoordinatesEntity(
            longitude = this.coordinates.longitude,
            latitude = this.coordinates.latitude
        ),
        weather = WeatherEntity(
            id = this.weatherList[0].id,
            main = this.weatherList[0].main,
            description = this.weatherList[0].description,
            icon = this.weatherList[0].icon
        ),
        base = this.base,
        main = MainEntity(
            temperature = this.main.temperature,
            feelsLike = this.main.feelsLike,
            temperatureMin = this.main.temperatureMin,
            temperatureMax = this.main.temperatureMax,
            pressure = this.main.pressure,
            humidity = this.main.humidity,
            seaLevelPressure = this.main.seaLevelPressure,
            groundLevelPressure = this.main.groundLevelPressure
        ),
        visibility = this.visibility,
        wind = WindEntity(
            speed = this.wind.speed,
            directionDegrees = this.wind.directionDegrees,
            gust = this.wind.gust
        ),
        clouds = CloudsEntity(
            cloudiness = this.clouds.cloudiness
        ),
        timeOfDataCalculation = this.timeOfDataCalculation,
        sys = SysEntity(
            type = this.sys.type,
            id = this.sys.id,
            country = this.sys.country,
            sunrise = this.sys.sunrise,
            sunset = this.sys.sunset
        ),
        timezone = this.timezone,
        cityId = this.cityId,
        name = this.name,
        cod = this.cod
    )

fun OpenWeather.openWeatherToLocationWeatherEntity(): LocationWeatherEntity =
    LocationWeatherEntity(
        coordinates = CoordinatesEntity(
            longitude = this.coordinates.longitude,
            latitude = this.coordinates.latitude
        ),
        weather = WeatherEntity(
            id = this.weatherList[0].id,
            main = this.weatherList[0].main,
            description = this.weatherList[0].description,
            icon = this.weatherList[0].icon
        ),
        base = this.base,
        main = MainEntity(
            temperature = this.main.temperature,
            feelsLike = this.main.feelsLike,
            temperatureMin = this.main.temperatureMin,
            temperatureMax = this.main.temperatureMax,
            pressure = this.main.pressure,
            humidity = this.main.humidity,
            seaLevelPressure = this.main.seaLevelPressure,
            groundLevelPressure = this.main.groundLevelPressure
        ),
        visibility = this.visibility,
        wind = WindEntity(
            speed = this.wind.speed,
            directionDegrees = this.wind.directionDegrees,
            gust = this.wind.gust
        ),
        clouds = CloudsEntity(
            cloudiness = this.clouds.cloudiness
        ),
        timeOfDataCalculation = this.timeOfDataCalculation,
        sys = SysEntity(
            type = this.sys.type,
            id = this.sys.id,
            country = this.sys.country,
            sunrise = this.sys.sunrise,
            sunset = this.sys.sunset
        ),
        timezone = this.timezone,
        cityId = this.cityId,
        name = this.name,
        cod = this.cod
    )