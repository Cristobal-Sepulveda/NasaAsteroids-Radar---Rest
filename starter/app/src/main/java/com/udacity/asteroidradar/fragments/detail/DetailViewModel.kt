package com.udacity.asteroidradar.fragments.detail

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.network.Asteroid

class DetailViewModel(asteroid: Asteroid, app: Application): AndroidViewModel(app){
    private val _selectedAsteroid = MutableLiveData<Asteroid>()
    val selectedAsteroid: LiveData<Asteroid>
        get()= _selectedAsteroid

    init{
        _selectedAsteroid.value = asteroid
    }
}

/**
 * Simple ViewModel factory that provides the Asteroid and context to the ViewModel.
 */
class DetailViewModelFactory(
        private val asteroid:Asteroid,
        private val application: Application) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(asteroid, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}