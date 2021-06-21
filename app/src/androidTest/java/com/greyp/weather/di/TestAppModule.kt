package com.greyp.weather.di

import android.content.Context
import androidx.room.Room
import com.greyp.weather.data.local.GreypWeatherDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object TestAppModule {

    @Provides
    @Named("test_database")
    fun provideInMemoryDb(@ApplicationContext context: Context) =
        Room.inMemoryDatabaseBuilder(context, GreypWeatherDatabase::class.java)
            .allowMainThreadQueries()
            .build()

}