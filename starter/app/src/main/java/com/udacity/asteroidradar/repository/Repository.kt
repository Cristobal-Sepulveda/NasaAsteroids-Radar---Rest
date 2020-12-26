package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.database.DATABASE
import com.udacity.asteroidradar.network.Asteroid
import com.udacity.asteroidradar.network.AsteroidsApi
import com.udacity.asteroidradar.network.ImageApi
import com.udacity.asteroidradar.network.parseAsteroidsJsonResult
import com.udacity.asteroidradar.objects.dataTransferObjects.NetworkDailyImage
import com.udacity.asteroidradar.objects.dataTransferObjects.NetworkDailyImageContainer
import com.udacity.asteroidradar.objects.dataTransferObjects.asDatabaseModel
import com.udacity.asteroidradar.objects.databaseObjects.asDomainModel
import com.udacity.asteroidradar.objects.domainObjects.DailyImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
lateinit var imageLoadError: NetworkDailyImageContainer
class Repository(private val database: DATABASE) {

    val asteroidsFromDatabase:LiveData<List<Asteroid>> =
            Transformations.map(database.asteroidsDao.getAsteroids()){
                it.asDomainModel()
            }
    val dailyImageFromDatabase: LiveData<List<DailyImage>> =
            Transformations.map(database.imageDao.getImage()){
                it.asDomainModel()
            }

    suspend fun refreshDATABASE(){
        val listOfAsteroids = AsteroidsApi.retrofitService.getAsteroids()
        val listOfAdaptedAsteroids = parseAsteroidsJsonResult(JSONObject(listOfAsteroids.toString()))
        withContext(Dispatchers.IO) {
            database.asteroidsDao.insertAllAsteroids(*listOfAdaptedAsteroids.asDatabaseModel())

            ImageApi.retrofitService.getImage().enqueue(
                    object: Callback<NetworkDailyImageContainer>{
                        override fun onResponse(call: Call<NetworkDailyImageContainer>,
                                                response: Response<NetworkDailyImageContainer>) {
                            database.imageDao.insertImage(*response.body()!!.asDatabaseModel())
                        }
                        override fun onFailure(call: Call<NetworkDailyImageContainer>,
                                               t: Throwable) {
                            val image = NetworkDailyImage("20-20-2020",
                                    "fail Load",
                                    "asd",
                                    "image",
                                    "asd",
                                    "fail load",
                                    "${t.message}")
                            imageLoadError = NetworkDailyImageContainer(listOf(image))
                            database.imageDao.insertImage(*imageLoadError.asDatabaseModel())
                        }
                    })
        }
    }
}