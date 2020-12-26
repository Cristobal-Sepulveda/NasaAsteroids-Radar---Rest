package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.database.DATABASE
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.network.parseAsteroidsJsonResult
import com.udacity.asteroidradar.network.AsteroidsApi
import com.udacity.asteroidradar.objects.domainObjects.DailyImage
import com.udacity.asteroidradar.network.ImageApi
import com.udacity.asteroidradar.objects.dataTransferObjects.NetworkAsteroid
import com.udacity.asteroidradar.repository.Repository
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val repository = Repository(database)

    init{
        viewModelScope.launch{
            repository.refreshDATABASE()
        }
    }

    val domainAsteroid = repository.asteroidsFromDatabase
    val domainDailyImage = repository.dailyImageFromDatabase.value?.last()?.url


}