package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.Repository
import kotlinx.coroutines.launch

enum class AsteroidsApiStatus{LOADING, ERROR, DONE}
class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val repository = Repository(database)
    private val _status = MutableLiveData<AsteroidsApiStatus>()
    val status: LiveData<AsteroidsApiStatus>
        get()= _status

    init{
        viewModelScope.launch{
            _status.value = AsteroidsApiStatus.LOADING
            repository.refreshDATABASE()
            _status.value = AsteroidsApiStatus.DONE
        }
    }

    val domainAsteroid = repository.asteroidsFromDatabase
    val domainDailyImage = repository.parsed?.url

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