package com.greyp.weather.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.greyp.weather.R
import com.greyp.weather.databinding.FragmentHomeBinding
import com.greyp.weather.ui.adapters.ViewPagerAdapter
import com.greyp.weather.ui.fragments.viewpager.CityFragment
import com.greyp.weather.ui.fragments.viewpager.LocationFragment
import com.greyp.weather.utils.viewBinding


class HomeFragment : Fragment(R.layout.fragment_home) {

    private val binding by viewBinding(FragmentHomeBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewPager()
        setupTabLayout()
    }

    private fun setupTabLayout() {
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> {
                    Log.d(TAG, "setupTabLayout - selected: 'City'")
                    tab.text = getString(R.string.city)
                }
                1 -> {
                    Log.d(TAG, "setupTabLayout - selected: 'Location'")
                    tab.text = getString(R.string.location)
                }
            }
        }.attach()
    }

    private fun setupViewPager() {
        val viewPagerAdapter = ViewPagerAdapter(
            fragmentManager = requireActivity().supportFragmentManager,
            lifecycle = lifecycle,
            fragments = arrayListOf(CityFragment(), LocationFragment())
        )
        binding.viewPager.adapter = viewPagerAdapter
    }

    companion object {
        private const val TAG = "HomeFragment"
    }
}