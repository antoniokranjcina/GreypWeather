package com.greyp.weather.ui.fragments.viewpager

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.app.AlertDialog
import android.content.IntentSender
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
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
import com.google.android.material.snackbar.Snackbar
import com.greyp.weather.R
import com.greyp.weather.data.local.entities.LocationWeatherEntity
import com.greyp.weather.databinding.FragmentLocationBinding
import com.greyp.weather.ui.viewmodels.LocationWeatherViewModel
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
class LocationFragment : Fragment(R.layout.fragment_location) {

    private val binding by viewBinding(FragmentLocationBinding::bind)

    private val viewModel: LocationWeatherViewModel by viewModels()

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        getWeatherData()
        setupSwipeRefreshLayout()
        searchByLocation()
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

    private fun setupViewsVisibility(resource: Resource<LocationWeatherEntity>) {
        binding.apply {
            val hasData = resource.data != null
            val isLoading = resource.status == Status.LOADING

            swipeRefreshLayout.isRefreshing = isLoading
            textViewLocationInitMessage.isVisible = !hasData && !isLoading
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

    private fun displayErrorMessage(resource: Resource<LocationWeatherEntity>) {
        val snackbar = Snackbar.make(binding.root, resource.error!!, Snackbar.LENGTH_LONG)
        snackbar.setAction(getString(R.string.dismiss)) {
            snackbar.dismiss()
        }
        snackbar.show()
    }

    private fun displayWeatherData(resource: Resource<LocationWeatherEntity>) {
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

    private fun searchByLocation() {
        val latitude = sharedPreferences.getString(Constants.LAST_LOCATION_LATITUDE, Constants.NO_LOCATION_SAVED)
        val longitude = sharedPreferences.getString(Constants.LAST_LOCATION_LONGITUDE, Constants.NO_LOCATION_SAVED)

        requestPermission()
        askForGps()
        if (!latitude.isNullOrEmpty() && !longitude.isNullOrEmpty() && latitude != Constants.NO_LOCATION_SAVED && longitude != Constants.NO_LOCATION_SAVED) {
            viewModel.getWeatherByLocation(latitude = latitude, longitude = longitude)
        } else {
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun askForGps() {
        val locationRequest = LocationRequest.create().apply {
            fastestInterval = 1500
            interval = 300
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

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
                fusedLocationProviderClient.lastLocation.addOnSuccessListener {
                    val location = it ?: return@addOnSuccessListener
                    Log.d(TAG, "requestPermission: ${location.longitude} ${location.latitude}")
                    val editor = sharedPreferences.edit()
                    editor.putString(Constants.LAST_LOCATION_LONGITUDE, "${location.longitude}").apply()
                    editor.putString(Constants.LAST_LOCATION_LATITUDE, "${location.latitude}").apply()
                }
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
            .setPositiveButton(getString(R.string.ok)) { _, _ ->
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
    }

}