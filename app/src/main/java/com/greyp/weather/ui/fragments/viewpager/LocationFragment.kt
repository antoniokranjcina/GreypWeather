package com.greyp.weather.ui.fragments.viewpager

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.app.AlertDialog
import android.content.IntentSender
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.greyp.weather.R
import com.greyp.weather.data.remote.responses.OpenWeather
import com.greyp.weather.databinding.FragmentLocationBinding
import com.greyp.weather.ui.viewmodels.WeatherViewModel
import com.greyp.weather.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.math.roundToInt


@AndroidEntryPoint
class LocationFragment : Fragment(R.layout.fragment_location), View.OnClickListener {

    private val binding by viewBinding(FragmentLocationBinding::bind)

    private val viewModel: WeatherViewModel by viewModels()

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private var locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val locationList = locationResult.locations
            if (locationList.isNotEmpty()) {
                // The last location in the list is the newest
                val lastLocation = locationList.last()
                if (lastLocation != null) {
                    val editor = sharedPreferences.edit()
                    editor.putString(Constants.LAST_LOCATION_LONGITUDE, "${lastLocation.longitude}").apply()
                    editor.putString(Constants.LAST_LOCATION_LATITUDE, "${lastLocation.latitude}").apply()
                    Log.d(TAG, "onLocationResult - longitude: ${lastLocation.longitude}")
                    Log.d(TAG, "onLocationResult - latitude: ${lastLocation.latitude}")
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        setupOnClickListeners()
        getWeatherData()
        setupSwipeRefreshLayout()
        searchByLocation()
    }

    override fun onPause() {
        super.onPause()
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    override fun onClick(v: View?) {
        when (v!!) {
            binding.buttonRetry -> {
                searchByLocation()
            }
        }
    }

    private fun setupOnClickListeners() {
        binding.apply {
            buttonRetry.setOnClickListener(this@LocationFragment)
        }
    }

    private fun setupSwipeRefreshLayout() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            searchByLocation()
        }
    }

    private fun getWeatherData() {
        lifecycleScope.launchWhenCreated {
            viewModel.weatherByLocation.collect {
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

    private fun searchByLocation() {
        val latitude = sharedPreferences.getString(Constants.LAST_LOCATION_LATITUDE, Constants.NO_LOCATION_SAVED)
        val longitude = sharedPreferences.getString(Constants.LAST_LOCATION_LONGITUDE, Constants.NO_LOCATION_SAVED)

        if (!latitude.isNullOrEmpty() && !longitude.isNullOrEmpty() && latitude != Constants.NO_LOCATION_SAVED && longitude != Constants.NO_LOCATION_SAVED) {
            viewModel.getWeatherByLocation(latitude = latitude, longitude = longitude)
        } else {
            binding.swipeRefreshLayout.isRefreshing = false
            requestPermission()
            askForGps()
        }
    }

    private fun askForGps() {
        val builder: LocationSettingsRequest.Builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val result: Task<LocationSettingsResponse> = LocationServices.getSettingsClient(requireContext()).checkLocationSettings(builder.build())

        result.addOnCompleteListener { task ->
            try {
                task.getResult(ApiException::class.java)
            } catch (e: ApiException) {
                when (e.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                        try {
                            val resolvableApiException: ResolvableApiException = e as ResolvableApiException
                            resolvableApiException.startResolutionForResult(requireActivity(), Constants.REQUEST_CHECK_CODE)
                        } catch (ex: IntentSender.SendIntentException) {
                            ex.printStackTrace()
                        } catch (ex: ClassCastException) {
                            ex.printStackTrace()
                        }
                    }

                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                    }
                }
            }
        }
    }

    private fun requestPermission() {
        when {
            ContextCompat.checkSelfPermission(requireContext(), ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED -> {
                // Permission is granted
                Log.d(TAG, "requestPermission: granted.")
                fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
            }
            ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), ACCESS_FINE_LOCATION) -> {
                // Show dialog
                Log.d(TAG, "requestPermission: show permission dialog.")
                dialog(title = getString(R.string.alert_dialog_title), message = getString(R.string.alert_dialog_message), launcher = requestPermissionLauncher)
            }
            else -> {
                // Permission has not been asked yet
                Log.d(TAG, "requestPermission: permission asked.")
                requestPermissionLauncher.launch(ACCESS_FINE_LOCATION)
            }
        }
    }

    private fun dialog(title: String, message: String, launcher: ActivityResultLauncher<String>?) {
        AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK") { _, _ ->
                Log.d(TAG, "dialog: 'OK' clicked.")
                launcher?.launch(ACCESS_FINE_LOCATION)
            }
            .create()
            .show()
    }

    val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            Log.d(TAG, "requestPermissionLauncher: granted.")
            requestPermission()
        } else {
            if (!shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {
                Log.d(TAG, "requestPermissionLauncher: permanently denied.")
                dialog(title = getString(R.string.alert_dialog_title_denied), message = getString(R.string.alert_dialog_message_denied), launcher = null)
            } else {
                Log.d(TAG, "requestPermissionLauncher: denied.")
            }
        }
    }

    companion object {
        private const val TAG = "LocationFragment"

        private val locationRequest = LocationRequest.create().apply {
            fastestInterval = 1500
            interval = 300
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

}