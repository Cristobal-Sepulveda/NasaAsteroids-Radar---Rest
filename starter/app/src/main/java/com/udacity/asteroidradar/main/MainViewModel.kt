package com.udacity.asteroidradar.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.network.Asteroid
import com.udacity.asteroidradar.network.AsteroidsApi
import org.json.JSONObject
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
/*        val jsonResult= AsteroidsApi.retrofitService.getAsteroids()
        var asteroids = parseAsteroidsJsonResult(JSONObject(jsonResult.toString())*/
        AsteroidsApi.retrofitService.getAsteroids().enqueue(object: Callback<String>{
            override fun onFailure(call: Call<String>, t: Throwable) {
                _response.value = "Failure: " + t.message
            }
            override fun onResponse(call: Call<String>, response: Response<String>) {
                var list = parseAsteroidsJsonResult(JSONObject(response.body()!!))

                _response.value = list.toString() //response.body()
            }
        })
    }

    init{
        getAsteroidsFromNeoWs()
    }
}