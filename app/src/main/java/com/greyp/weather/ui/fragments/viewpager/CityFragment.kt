package com.greyp.weather.ui.fragments.viewpager

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.greyp.weather.R
import com.greyp.weather.data.local.entities.CityWeatherEntity
import com.greyp.weather.databinding.FragmentCityBinding
import com.greyp.weather.ui.viewmodels.CityWeatherViewModel
import com.greyp.weather.utils.Constants
import com.greyp.weather.utils.Resource
import com.greyp.weather.utils.Status
import com.greyp.weather.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.math.roundToInt


@AndroidEntryPoint
class CityFragment : Fragment(R.layout.fragment_city), SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener {

    private val binding by viewBinding(FragmentCityBinding::bind)

    private val viewModel: CityWeatherViewModel by viewModels()

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchByCityName()
        setHasOptionsMenu(true)
        getWeatherData()
        setupSwipeRefreshLayout()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_home_fragment, menu)

        val searchItem = menu.findItem(R.id.item_search)
        if (searchItem != null) {
            searchItem.setOnActionExpandListener(this)
            val searchView = searchItem.actionView as SearchView
            searchView.setOnQueryTextListener(this)
        }

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_search -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        Log.d(TAG, "onQueryTextSubmit: $query")

        if (!query.isNullOrEmpty()) {
            val editor = sharedPreferences.edit()
            editor.putString(Constants.LAST_SELECTED_CITY, query).apply()
            viewModel.getWeatherByCityName(cityName = query)
        }

        return query.isNullOrEmpty()
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        Log.d(TAG, "onQueryTextChange: $newText")
        return newText.isNullOrEmpty()
    }

    override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
        Log.d(TAG, "onMenuItemActionExpand - pressed: 'searchView'")
        return true
    }

    override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
        Log.d(TAG, "onMenuItemActionExpand - closed: 'searchView'")
        return true
    }

    private fun setupSwipeRefreshLayout() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            searchByCityName()
        }
    }

    private fun getWeatherData() {
        lifecycleScope.launchWhenCreated {
            viewModel.weatherByCityName.collect {
                setupViewsVisibility(it)

                when (it.status) {
                    Status.EMPTY -> {
                        Log.d(TAG, "getWeatherData: empty")
                    }
                    Status.LOADING -> {
                        Log.d(TAG, "getWeatherData: loading")
                    }
                    Status.ERROR -> {
                        Log.d(TAG, "getWeatherData: error - ${it.error}")
                        displayErrorMessage(it)
                        displayWeatherData(it)
                    }
                    Status.SUCCESS -> {
                        Log.d(TAG, "getWeatherData: success - data loaded - ${it.data}")
                        displayWeatherData(it)
                    }
                }
            }
        }
    }

    private fun setupViewsVisibility(resource: Resource<CityWeatherEntity>) {
        binding.apply {
            val hasData = resource.data != null
            val isLoading = resource.status == Status.LOADING

            swipeRefreshLayout.isRefreshing = isLoading
            textViewCityInitMessage.isVisible = !hasData && !isLoading
            textViewCity.isVisible = hasData
            textViewUpdatedAt.isVisible = hasData
            view1.isVisible = hasData
            textViewTemperatureText.isVisible = hasData
            textViewCurrentTemp.isVisible = hasData
            textViewMinMaxTemp.isVisible = hasData
            textViewDescription.isVisible = hasData
            view2.isVisible = hasData
            textViewHumidityText.isVisible = hasData
            progressBarHumidity.isVisible = hasData
            textViewHumidityPercentage.isVisible = hasData
            view3.isVisible = hasData
            textViewWindText.isVisible = hasData
            textViewWindDirectionText.isVisible = hasData
            textViewWindDirection.isVisible = hasData
            textViewWindSpeedText.isVisible = hasData
            textViewWindSpeed.isVisible = hasData
            view4.isVisible = hasData
            textViewSunriseSunset.isVisible = hasData
            textViewSunriseText.isVisible = hasData
            textViewSunrise.isVisible = hasData
            textViewSunsetText.isVisible = hasData
            textViewSunset.isVisible = hasData
            view5.isVisible = hasData
            textViewClouds.isVisible = hasData
            progressBarCloudiness.isVisible = hasData
            textViewCloudiness.isVisible = hasData
            view6.isVisible = hasData
        }
    }

    private fun displayErrorMessage(resource: Resource<CityWeatherEntity>) {
        val snackbar = Snackbar.make(binding.root, resource.error!!, Snackbar.LENGTH_LONG)
        snackbar.setAction(getString(R.string.dismiss)) {
            snackbar.dismiss()
        }
        snackbar.show()
    }

    private fun displayWeatherData(resource: Resource<CityWeatherEntity>) {
        val result = resource.data ?: return
        val main = result.main
        val wind = result.wind
        val sys = result.sys
        val clouds = result.clouds

        val degree = getString(R.string.degree_symbol)
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())

        binding.apply {
            textViewCity.text = result.name
            val updatedAt = "${getString(R.string.updated_at)} ${sdf.format(Date(result.updatedAt))}h"
            textViewUpdatedAt.text = updatedAt

            val currentTemp = "${main.temperature.roundToInt()}$degree"
            val minMaxTemp = "${main.temperatureMin.roundToInt()}$degree/${main.temperatureMax.roundToInt()}$degree"
            textViewCurrentTemp.text = currentTemp
            textViewMinMaxTemp.text = minMaxTemp
            textViewDescription.text = result.weather.description

            val humidityPercentage = "${main.humidity}%"
            progressBarHumidity.progress = main.humidity
            textViewHumidityPercentage.text = humidityPercentage

            val windDirection = "${wind.directionDegrees}$degree"
            val windSpeed = "${wind.speed} m/s"
            textViewWindDirection.text = windDirection
            textViewWindSpeed.text = windSpeed

            val sunrise = "${sdf.format(Date(sys.sunrise * 1000))}h"
            val sunset = "${sdf.format(Date(sys.sunset * 1000))}h"
            textViewSunrise.text = sunrise
            textViewSunset.text = sunset

            val cloudiness = "${clouds.cloudiness}%"
            progressBarCloudiness.progress = clouds.cloudiness
            textViewCloudiness.text = cloudiness
        }
    }

    private fun searchByCityName() {
        val cityName = sharedPreferences.getString(Constants.LAST_SELECTED_CITY, Constants.NO_CITY_SAVED)
        if (cityName != Constants.NO_CITY_SAVED) {
            viewModel.getWeatherByCityName(cityName = cityName!!)
        } else {
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    companion object {
        private const val TAG = "CityFragment"
    }
}