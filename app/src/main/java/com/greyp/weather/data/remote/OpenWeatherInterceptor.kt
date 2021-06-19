package com.greyp.weather.data.remote

import com.greyp.weather.utils.Constants
import okhttp3.Interceptor
import okhttp3.Response


class OpenWeatherInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val url = request.url
            .newBuilder()
            .addQueryParameter("APPID", Constants.API_KEY)
            .addQueryParameter("units", "metric")
            .build()

        val apiRequest = request
            .newBuilder()
            .url(url)
            .build()

        return chain.proceed(apiRequest)
    }
}