package com.greyp.weather.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.greyp.weather.R
import com.greyp.weather.databinding.ActivityMainBinding
import com.greyp.weather.utils.ConnectionLiveData
import com.greyp.weather.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityMainBinding::inflate)

    private lateinit var navController: NavController

    private lateinit var connectionLiveData: ConnectionLiveData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_GreypWeather)
        setContentView(binding.root)

        observeInternetConnection()
        setNavHost()
        setToolbar()
    }

    private fun observeInternetConnection() {
        connectionLiveData = ConnectionLiveData(context = this)
        connectionLiveData.observe(this, { isNetworkAvailable ->
            Log.d(TAG, "observeInternetConnection: $isNetworkAvailable")
            binding.textViewNetworkBanner.isVisible = !isNetworkAvailable
        })
    }

    private fun setNavHost() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navigation_host) as NavHostFragment
        navController = navHostFragment.findNavController()
    }

    private fun setToolbar() {
        (this as AppCompatActivity).setSupportActionBar(binding.toolbar)
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}