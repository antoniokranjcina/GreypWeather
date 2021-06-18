package com.greyp.weather.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.greyp.weather.data.remote.responses.OpenWeather
import com.greyp.weather.data.repository.Repository
import com.greyp.weather.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private val _weatherByCityName = MutableStateFlow<Resource<OpenWeather>>(Resource.empty())
    val weatherByCityName: StateFlow<Resource<OpenWeather>> = _weatherByCityName

    private val _weatherByLocation = MutableStateFlow<Resource<OpenWeather>>(Resource.empty())
    val weatherByLocation: StateFlow<Resource<OpenWeather>> = _weatherByLocation

    fun getWeatherByCityName(cityName: String) = viewModelScope.launch {
        _weatherByCityName.value = Resource.loading(data = null)
        _weatherByCityName.value = repository.getWeatherByCityName(cityName = cityName)
    }

    fun getWeatherByLocation(longitude: String, latitude: String) = viewModelScope.launch {
        _weatherByLocation.value = Resource.loading(data = null)
        _weatherByLocation.value = repository.getWeatherByLocation(longitude = longitude, latitude = latitude)
    }

}