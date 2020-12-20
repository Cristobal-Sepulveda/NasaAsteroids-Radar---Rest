package com.udacity.asteroidradar.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.udacity.asteroidradar.network.Asteroid
import com.udacity.asteroidradar.network.AsteroidsApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    // The internal MutableLiveData String that stores the most recent response
    private val _response = MutableLiveData<String>()

    // The external immutable LiveData for the response String
    val response: LiveData<String>
        get() = _response

    private fun getAsteroidsFromNeoWs(){
        AsteroidsApi.retrofitService.getAsteroids().enqueue(object: Callback<List<Asteroid>>{
            override fun onFailure(call: Call<List<Asteroid>>, t: Throwable) {
                _response.value = "Failure: " + t.message
            }
            override fun onResponse(call: Call<List<Asteroid>>, response: Response<List<Asteroid>>) {
                _response.value = "Success : ${response.body()?.size} asteroids near Earth detected"
            }
        })
    }

    init{
        getAsteroidsFromNeoWs()
    }
}