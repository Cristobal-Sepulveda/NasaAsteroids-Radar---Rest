package com.udacity.asteroidradar.fragments.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.network.Asteroid
import com.udacity.asteroidradar.objects.databaseObjects.asDomainModel
import com.udacity.asteroidradar.repository.Repository
import com.udacity.asteroidradar.utils.getNextSevenDaysFormattedDates
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

    init {
        viewModelScope.launch {
            _status.value = AsteroidsApiStatus.LOADING
            try {
                repository.refreshDATABASE()
                _status.value = AsteroidsApiStatus.DONE
            }catch(e:Exception){
                if(todayAsteroids.value == null){
                    _status.value = AsteroidsApiStatus.ERROR
                }else{
                    database.asteroidsDao.deleteOldsAsteroids(getNextSevenDaysFormattedDates().first())
                    _status.value = AsteroidsApiStatus.DONE
                }
            }
        }
    }
    var domainAsteroidsInScreen: MutableLiveData<List<Asteroid>> =
            repository.asteroidsFromDatabase as MutableLiveData<List<Asteroid>>

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
