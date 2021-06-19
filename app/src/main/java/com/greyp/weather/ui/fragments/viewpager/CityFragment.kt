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
import com.greyp.weather.R
import com.greyp.weather.data.remote.responses.OpenWeather
import com.greyp.weather.databinding.FragmentCityBinding
import com.greyp.weather.ui.viewmodels.WeatherViewModel
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
class CityFragment : Fragment(R.layout.fragment_city), SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener, View.OnClickListener {

    private val binding by viewBinding(FragmentCityBinding::bind)

    private val viewModel: WeatherViewModel by viewModels()

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchByCityName()
        setHasOptionsMenu(true)
        setupOnClickListeners()
        getWeatherData()
        setupSwipeRefreshLayout()

    }

    override fun onClick(v: View?) {
        when (v!!) {
            binding.buttonRetry -> {
                searchByCityName()
            }
        }
    }

    private fun setupOnClickListeners() {
        binding.apply {
            buttonRetry.setOnClickListener(this@CityFragment)
        }
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
                        error(it)
                    }
                    Status.SUCCESS -> {
                        Log.d(TAG, "getWeatherData: success - data loaded - ${it.data}")
                        success(it)
                    }
                }
            }
        }
    }

    private fun setupViewsVisibility(resource: Resource<OpenWeather>) {
        binding.apply {
            swipeRefreshLayout.isRefreshing = resource.status == Status.LOADING
            textViewError.isVisible = resource.status == Status.ERROR
            buttonRetry.isVisible = resource.status == Status.ERROR

            textViewCity.isVisible = resource.status == Status.SUCCESS

            view1.isVisible = resource.status == Status.SUCCESS
            textViewTemperatureText.isVisible = resource.status == Status.SUCCESS
            textViewCurrentTemp.isVisible = resource.status == Status.SUCCESS
            textViewMinMaxTemp.isVisible = resource.status == Status.SUCCESS
            textViewDescription.isVisible = resource.status == Status.SUCCESS

            view2.isVisible = resource.status == Status.SUCCESS
            textViewHumidityText.isVisible = resource.status == Status.SUCCESS
            progressBarHumidity.isVisible = resource.status == Status.SUCCESS
            textViewHumidityPercentage.isVisible = resource.status == Status.SUCCESS

            view3.isVisible = resource.status == Status.SUCCESS
            textViewWindText.isVisible = resource.status == Status.SUCCESS
            textViewWindDirectionText.isVisible = resource.status == Status.SUCCESS
            textViewWindDirection.isVisible = resource.status == Status.SUCCESS
            textViewWindSpeedText.isVisible = resource.status == Status.SUCCESS
            textViewWindSpeed.isVisible = resource.status == Status.SUCCESS

            view4.isVisible = resource.status == Status.SUCCESS
            textViewSunriseSunset.isVisible = resource.status == Status.SUCCESS
            textViewSunriseText.isVisible = resource.status == Status.SUCCESS
            textViewSunrise.isVisible = resource.status == Status.SUCCESS
            textViewSunsetText.isVisible = resource.status == Status.SUCCESS
            textViewSunset.isVisible = resource.status == Status.SUCCESS

            view5.isVisible = resource.status == Status.SUCCESS
            textViewClouds.isVisible = resource.status == Status.SUCCESS
            progressBarCloudiness.isVisible = resource.status == Status.SUCCESS
            textViewCloudiness.isVisible = resource.status == Status.SUCCESS

            view6.isVisible = resource.status == Status.SUCCESS
        }
    }

    private fun error(resource: Resource<OpenWeather>) {
        binding.apply {
            textViewError.text = resource.error
        }
    }

    private fun success(resource: Resource<OpenWeather>) {
        val result = resource.data ?: return
        val main = result.main
        val wind = result.wind
        val sys = result.sys
        val clouds = result.clouds

        val degree = getString(R.string.degree_symbol)

        binding.apply {
            textViewCity.text = result.name

            val currentTemp = "${main.temperature.roundToInt()}$degree"
            val minMaxTemp = "${main.temperatureMin.roundToInt()}$degree/${main.temperatureMax.roundToInt()}$degree"
            textViewCurrentTemp.text = currentTemp
            textViewMinMaxTemp.text = minMaxTemp
            textViewDescription.text = result.weatherList[0].description

            val humidityPercentage = "${main.humidity}%"
            progressBarHumidity.progress = main.humidity
            textViewHumidityPercentage.text = humidityPercentage

            val windDirection = "${wind.directionDegrees}$degree"
            val windSpeed = "${wind.speed} m/s"
            textViewWindDirection.text = windDirection
            textViewWindSpeed.text = windSpeed

            val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
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
        private const val TAG = "HomeFragment"
    }
}