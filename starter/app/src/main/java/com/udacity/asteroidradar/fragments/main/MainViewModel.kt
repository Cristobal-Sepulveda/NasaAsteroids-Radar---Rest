package com.udacity.asteroidradar.fragments.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.network.Asteroid
import com.udacity.asteroidradar.objects.databaseObjects.asDomainModel
import com.udacity.asteroidradar.repository.Repository
import kotlinx.coroutines.launch

enum class AsteroidsApiStatus{LOADING, ERROR, DONE}
class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val repository = Repository(database)

    private val _status = MutableLiveData<AsteroidsApiStatus>()
    val status: LiveData<AsteroidsApiStatus>
        get()= _status

    private val _navigateToSelectedAsteroid = MutableLiveData<Asteroid>()
    val navigateToSelectedAsteroid: LiveData<Asteroid>
        get() = _navigateToSelectedAsteroid

    var weekAsteroids = repository.asteroidsFromDatabase
    var todayAsteroids= repository.todayAsteroids

    var domainDailyImageUrl = repository.parsed?.url
    var domainDailyImageExplanation = repository.parsed?.explanation
    private val _domainAsteroidsInScreen = MutableLiveData<LiveData<List<Asteroid>>>()
    val domainAsteroidsInScreen: LiveData<LiveData<List<Asteroid>>>
        get()= _domainAsteroidsInScreen



    init{
        viewModelScope.launch{
            _status.value = AsteroidsApiStatus.LOADING
            repository.refreshDATABASE()
            _status.value = AsteroidsApiStatus.DONE
        println("${repository.dailyImageFromDatabase.value?.url} holaaa")
        }
        _domainAsteroidsInScreen.value = weekAsteroids
    }


    //<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>
    /** THESES ARE FOR NAVIGATE TO DETAILS FRAGMENT **/
    fun displayAsteroidDetails(asteroid: Asteroid) {
        _navigateToSelectedAsteroid.value = asteroid
    }

    fun displayAsteroidDetailsComplete() {
        _navigateToSelectedAsteroid.value = null
    }
    //>>>>>>>>>>>>>>>>>>>>>>>><<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**
     * Factory for constructing MainViewModel with parameter
     */
    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}