package com.greyp.weather.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.greyp.weather.data.local.entities.LocationWeatherEntity
import com.greyp.weather.data.repository.WeatherRepository
import com.greyp.weather.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationWeatherViewModel @Inject constructor(private val repository: WeatherRepository) : ViewModel() {

    private val _weatherByLocation = MutableStateFlow<Resource<LocationWeatherEntity>>(Resource.empty())
    val weatherByLocation: StateFlow<Resource<LocationWeatherEntity>> = _weatherByLocation

    fun getWeatherByLocation(longitude: String, latitude: String) = viewModelScope.launch {
        _weatherByLocation.value = Resource.loading(data = null)
        _weatherByLocation.value = repository.getWeatherByLocation(longitude = longitude, latitude = latitude)
    }

}