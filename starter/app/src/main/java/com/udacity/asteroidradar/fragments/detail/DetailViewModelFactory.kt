package com.udacity.asteroidradar.fragments.detail

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.udacity.asteroidradar.objects.domainObjects.Asteroid

/**
 * Simple ViewModel factory that provides the Asteroid and context to the ViewModel.
 */
class DetailViewModelFactory(
        private val asteroid: Asteroid,
        private val application: Application) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(asteroid, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}