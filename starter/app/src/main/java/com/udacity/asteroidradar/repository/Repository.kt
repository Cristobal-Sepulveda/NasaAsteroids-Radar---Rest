package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.database.DATABASE
import com.udacity.asteroidradar.network.*
import com.udacity.asteroidradar.objects.dataTransferObjects.*
import com.udacity.asteroidradar.objects.databaseObjects.asDomainModel
import com.udacity.asteroidradar.objects.domainObjects.DailyImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.await

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
        withContext(Dispatchers.IO) {
            val asteroidsList = AsteroidsApi.retrofitService.getAsteroids().await()
            val asteroidsParsed = parseAsteroidsJsonResult(JSONObject(asteroidsList))
            database.asteroidsDao.insertAllAsteroids(*asteroidsParsed.asDatabaseModel())
            val dailyImageResponse = DailyImageApi.retrofitService.getImage().await()
            database.imageDao.insertImage(*dailyImageResponse.asDatabaseModel())
        }
    }
}