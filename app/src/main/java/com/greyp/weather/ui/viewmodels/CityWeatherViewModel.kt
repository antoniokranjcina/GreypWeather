package com.greyp.weather.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.greyp.weather.data.local.entities.CityWeatherEntity
import com.greyp.weather.data.repository.Repository
import com.greyp.weather.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CityWeatherViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private val _weatherByCityName = MutableStateFlow<Resource<CityWeatherEntity>>(Resource.empty())
    val weatherByCityName: StateFlow<Resource<CityWeatherEntity>> = _weatherByCityName

    fun getWeatherByCityName(cityName: String) = viewModelScope.launch {
        _weatherByCityName.value = Resource.loading(data = null)
        _weatherByCityName.value = repository.getWeatherByCityName(cityName = cityName)
    }
}