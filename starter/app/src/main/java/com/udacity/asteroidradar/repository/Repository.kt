package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.database.DATABASE
import com.udacity.asteroidradar.network.*
import com.udacity.asteroidradar.objects.dataTransferObjects.*
import com.udacity.asteroidradar.objects.databaseObjects.DatabaseDailyImageEntity
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
    val dailyImageFromDatabase: LiveData<DatabaseDailyImageEntity> = database.dailyImageDao.getImage()
    val parsed= dailyImageFromDatabase.value?.asDomainModel(dailyImageFromDatabase.value!!)

    suspend fun refreshDATABASE(){
        val dailyImageResponse = DailyImageApi.retrofitService.getImage().await()
        val asteroidsList = AsteroidsApi.retrofitService.getAsteroids().await()
        val asteroidsParsed = parseAsteroidsJsonResult(JSONObject(asteroidsList))
        withContext(Dispatchers.IO) {
            database.asteroidsDao.insertAllAsteroids(*asteroidsParsed.asDatabaseModel())
            database.dailyImageDao.insertImage(dailyImageResponse.asDatabaseModel(dailyImageResponse))
        }

    }
}