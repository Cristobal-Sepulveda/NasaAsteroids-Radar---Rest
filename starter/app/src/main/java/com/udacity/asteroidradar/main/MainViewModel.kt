package com.udacity.asteroidradar.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.udacity.asteroidradar.network.parseAsteroidsJsonResult
import com.udacity.asteroidradar.network.Asteroid
import com.udacity.asteroidradar.network.AsteroidsApi
import com.udacity.asteroidradar.objects.domainObjects.DailyImage
import com.udacity.asteroidradar.network.ImageApi
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    private val _response = MutableLiveData<List<Asteroid>>()
    val response: LiveData<List<Asteroid>>
        get() = _response

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    private val _dailyImage = MutableLiveData<DailyImage>()
    val dailyImage: LiveData<DailyImage>
        get() = _dailyImage
    /**************************************************************************************/
    private fun getAsteroidsFromNeoWs(){
    /*        viewModelScope.launch {
            val listResult = AsteroidsApi.retrofitService.getAsteroidsAsync()
            try {
                _response.value = listResult.toString()
            } catch (e: Exception) {
                _response.value = "Failure: ${e.message}"
            }
        }*/
        //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
        AsteroidsApi.retrofitService.getAsteroids().enqueue(object: Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                _error.value = "Failure: " + t.message
            }
            override fun onResponse(call: Call<String>, response: Response<String>) {
                val list = parseAsteroidsJsonResult(JSONObject(response.body()!!))
                _response.value = list.toList()
            }
        })
        //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        ImageApi.retrofitService.getImage().enqueue(object: Callback<DailyImage> {
            override fun onFailure(call: Call<DailyImage>, t: Throwable) {
                _error.value = "Failure: " + t.message
            }
            override fun onResponse(call: Call<DailyImage>, response: Response<DailyImage>) {
                println(response.body())
                _dailyImage.value = response.body()
            }
        })
        //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    }
    /**************************************************************************************/
    init{
        getAsteroidsFromNeoWs()
    }

}