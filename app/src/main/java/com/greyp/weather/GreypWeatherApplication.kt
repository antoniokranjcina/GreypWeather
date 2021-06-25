package com.greyp.weather

import android.app.Application
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.greyp.weather.utils.Constants
import com.greyp.weather.utils.WorkManagerHelper
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit

@HiltAndroidApp
class GreypWeatherApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        val workManager = WorkManager.getInstance(this)
        val request = PeriodicWorkRequest.Builder(WorkManagerHelper::class.java, 1, TimeUnit.HOURS, 15, TimeUnit.MINUTES)
            .setInitialDelay(1, TimeUnit.HOURS)
            .build()
        workManager.enqueueUniquePeriodicWork(Constants.UNIQUE_WORK_NAME, ExistingPeriodicWorkPolicy.KEEP, request)
    }
}