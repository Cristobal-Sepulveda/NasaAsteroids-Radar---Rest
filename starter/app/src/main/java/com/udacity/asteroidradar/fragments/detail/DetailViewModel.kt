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