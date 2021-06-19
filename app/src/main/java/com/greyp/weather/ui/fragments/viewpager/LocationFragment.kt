package com.greyp.weather.ui.fragments.viewpager

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.greyp.weather.R
import com.greyp.weather.databinding.FragmentLocationBinding
import com.greyp.weather.utils.viewBinding


class LocationFragment : Fragment(R.layout.fragment_location) {

    private val binding by viewBinding(FragmentLocationBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

        }
    }

}