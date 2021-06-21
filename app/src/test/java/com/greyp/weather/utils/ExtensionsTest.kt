package com.greyp.weather.utils

import com.google.common.truth.Truth.assertThat
import com.greyp.weather.data.local.entities.CityWeatherEntity
import com.greyp.weather.data.local.entities.LocationWeatherEntity
import com.greyp.weather.data.local.entities.embedded.*
import com.greyp.weather.data.remote.responses.*
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ExtensionsTest {

    @Test
    fun openWeatherToCityWeatherEntity_returnsEntity() {
        val currentTime = System.currentTimeMillis()
        val result = openWeather.openWeatherToCityWeatherEntity().apply {
            updatedAt = currentTime
        }
        cityWeather.updatedAt = currentTime
        assertThat(result).isEqualTo(cityWeather)
    }

    @Test
    fun openWeatherToLocationWeatherEntity_returnsEntity() {
        val currentTime = System.currentTimeMillis()
        val result = openWeather.openWeatherToLocationWeatherEntity().apply {
            updatedAt = currentTime
        }
        locationWeather.updatedAt = currentTime
        assertThat(result).isEqualTo(locationWeather)
    }

    companion object {
        val openWeather = OpenWeather(
            coordinates = Coordinates(
                longitude = 12.2,
                latitude = 54.2
            ),
            weatherList = listOf(
                Weather(
                    id = 12L,
                    main = "MyMain",
                    description = "Cold",
                    icon = "12l"
                )
            ),
            base = "Base",
            main = Main(
                temperatureMax = 32.0,
                temperatureMin = 23.0,
                temperature = 30.0,
                feelsLike = 40.0,
                pressure = 1200,
                humidity = 100,
                seaLevelPressure = 12,
                groundLevelPressure = 100
            ),
            visibility = 1000,
            clouds = Clouds(
                cloudiness = 1000
            ),
            wind = Wind(
                speed = 12.0,
                directionDegrees = 200.0,
                gust = 120.0
            ),
            rain = Rain(
                volumeIn1h = 12.0,
                volumeIn3h = 20.0
            ),
            snow = Snow(
                volumeIn3h = 22.0,
                volumeIn1h = 21.0
            ),
            timeOfDataCalculation = 100.0,
            sys = Sys(
                type = 10,
                id = 100,
                country = "HR",
                sunset = 102131231L,
                sunrise = 102131121L
            ),
            timezone = 12,
            cityId = 122,
            name = "Zagreb",
            cod = "Cod"
        )

        private val coordinates = CoordinatesEntity(
            longitude = 12.2,
            latitude = 54.2
        )
        private val weather = WeatherEntity(
            id = 12L,
            main = "MyMain",
            description = "Cold",
            icon = "12l"
        )
        private val main = MainEntity(
            temperatureMax = 32.0,
            temperatureMin = 23.0,
            temperature = 30.0,
            feelsLike = 40.0,
            pressure = 1200,
            humidity = 100,
            seaLevelPressure = 12,
            groundLevelPressure = 100
        )
        private val clouds = CloudsEntity(
            cloudiness = 1000
        )
        private val wind = WindEntity(
            speed = 12.0,
            directionDegrees = 200.0,
            gust = 120.0
        )
        private val sys = SysEntity(
            type = 10,
            id = 100,
            country = "HR",
            sunset = 102131231L,
            sunrise = 102131121L
        )

        val cityWeather = CityWeatherEntity(
            coordinates = coordinates,
            weather = weather,
            base = "Base",
            main = main,
            visibility = 1000,
            clouds = clouds,
            wind = wind,
            timeOfDataCalculation = 100.0,
            sys = sys,
            timezone = 12,
            cityId = 122,
            name = "Zagreb",
            cod = "Cod"
        )

        val locationWeather = LocationWeatherEntity(
            coordinates = coordinates,
            weather = weather,
            base = "Base",
            main = main,
            visibility = 1000,
            clouds = clouds,
            wind = wind,
            timeOfDataCalculation = 100.0,
            sys = sys,
            timezone = 12,
            cityId = 122,
            name = "Zagreb",
            cod = "Cod"
        )
    }
}