package com.greyp.weather.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.greyp.weather.data.local.GreypWeatherDatabase
import com.greyp.weather.data.local.daos.CityWeatherDao
import com.greyp.weather.data.local.daos.LocationWeatherDao
import com.greyp.weather.data.remote.OpenWeatherInterceptor
import com.greyp.weather.data.remote.OpenWeatherMapApi
import com.greyp.weather.utils.Constants
import com.greyp.weather.utils.Constants.API_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideOpenWeatherInterceptor() = OpenWeatherInterceptor()

    @Singleton
    @Provides
    fun provideOpenWeatherMapApi(interceptor: OpenWeatherInterceptor): OpenWeatherMapApi {
        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(OpenWeatherMapApi::class.java)
    }

    @Singleton
    @Provides
    fun provideSharedPreferences(application: Application): SharedPreferences =
        application.getSharedPreferences(Constants.OPEN_WEATHER_PREFERENCES, Context.MODE_PRIVATE)

    @Singleton
    @Provides
    fun provideGreypWeatherDatabase(@ApplicationContext applicationContext: Context): GreypWeatherDatabase =
        Room.databaseBuilder(
            applicationContext,
            GreypWeatherDatabase::class.java,
            Constants.DATABASE_NAME
        ).build()

    @Singleton
    @Provides
    fun provideCityWeatherDao(greypWeatherDatabase: GreypWeatherDatabase): CityWeatherDao = greypWeatherDatabase.cityWeatherDao()

    @Singleton
    @Provides
    fun provideLocationWeatherDao(greypWeatherDatabase: GreypWeatherDatabase): LocationWeatherDao = greypWeatherDatabase.locationWeatherDao()

}