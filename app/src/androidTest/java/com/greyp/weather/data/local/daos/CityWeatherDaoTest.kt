package com.greyp.weather.data.local.daos

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.greyp.weather.data.local.GreypWeatherDatabase
import com.greyp.weather.data.local.entities.CityWeatherEntity
import com.greyp.weather.data.local.entities.embedded.*
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@ExperimentalCoroutinesApi
@SmallTest
@HiltAndroidTest
class CityWeatherDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_database")
    lateinit var database: GreypWeatherDatabase
    private lateinit var dao: CityWeatherDao

    @Before
    fun setup() {
        hiltRule.inject()
        dao = database.cityWeatherDao()
    }

    @After
    fun closeDatabase() {
        database.close()
    }

    @Test
    fun insertCityWeatherTest() = runBlockingTest {
        dao.insertWeather(cityWeather = cityWeather)
        val result = dao.getWeather()

        assertThat(result).isEqualTo(cityWeather)
    }

    @Test
    fun deleteCityWeatherTest() = runBlockingTest {
        dao.apply {
            insertWeather(cityWeather = cityWeather)
            deleteAllWeather()
        }
        val result = dao.getWeather()

        assertThat(result).isNull()
    }

    companion object {
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
    }

}