package com.greyp.weather.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.greyp.weather.R
import com.greyp.weather.databinding.FragmentHomeBinding
import com.greyp.weather.utils.viewBinding

class HomeFragment : Fragment(R.layout.fragment_home), SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener {

    private val binding by viewBinding(FragmentHomeBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        binding.apply {

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

    companion object {
        private const val TAG = "HomeFragment"
    }
}