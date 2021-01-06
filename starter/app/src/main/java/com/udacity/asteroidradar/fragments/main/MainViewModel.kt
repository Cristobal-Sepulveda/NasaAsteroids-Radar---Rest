package com.udacity.asteroidradar.fragments.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.network.Asteroid
import com.udacity.asteroidradar.repository.Repository
import kotlinx.coroutines.launch

enum class AsteroidsApiStatus{LOADING, ERROR, DONE}
class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val repository = Repository(database)
    var todayAsteroids= repository.todayAsteroids
    var weekAsteroids = repository.asteroidsFromDatabase
    var domainDailyImageFromDatabase = repository.dailyImageFromDatabase

    private val _status = MutableLiveData<AsteroidsApiStatus>()
    val status: LiveData<AsteroidsApiStatus>
        get()= _status

    private val _navigateToSelectedAsteroid = MutableLiveData<Asteroid>()
    val navigateToSelectedAsteroid: LiveData<Asteroid>
        get() = _navigateToSelectedAsteroid

    //Using MediatorLiveData to observe Repo LiveData
    val domainAsteroidsInScreen: MediatorLiveData<List<Asteroid>> = MediatorLiveData()

    init {
        viewModelScope.launch {
            _status.value = AsteroidsApiStatus.LOADING
            try {
                repository.refreshDATABASE()

                domainAsteroidsInScreen.addSource(weekAsteroids){
                    domainAsteroidsInScreen.value = it
                }
                _status.value = AsteroidsApiStatus.DONE
            }catch(e:Exception){
                _status.value = AsteroidsApiStatus.ERROR
                domainAsteroidsInScreen.addSource(weekAsteroids){
                    domainAsteroidsInScreen.value = it
                }
            }
        }
    }

/*    var domainAsteroidsInScreen: MutableLiveData<List<Asteroid>> =
            repository.asteroidsFromDatabase as MutableLiveData<List<Asteroid>>*/

    //<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>
    /** THESES ARE FOR NAVIGATE TO DETAILS FRAGMENT **/
    fun displayAsteroidDetails(asteroid: Asteroid) {
        _navigateToSelectedAsteroid.value = asteroid
    }

    fun displayAsteroidDetailsComplete() {
        _navigateToSelectedAsteroid.value = null
    }
    //>>>>>>>>>>>>>>>>>>>>>>>><<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

}
