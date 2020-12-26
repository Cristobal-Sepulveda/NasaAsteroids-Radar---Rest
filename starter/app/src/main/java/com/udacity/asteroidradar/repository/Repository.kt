package com.udacity.asteroidradar.repository

import com.udacity.asteroidradar.database.DATABASE
import com.udacity.asteroidradar.network.AsteroidsApi
import com.udacity.asteroidradar.network.ImageApi
import com.udacity.asteroidradar.network.parseAsteroidsJsonResult
import com.udacity.asteroidradar.objects.dataTransferObjects.NetworkDailyImageContainer
import com.udacity.asteroidradar.objects.dataTransferObjects.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

lateinit var dailyImageContainer: NetworkDailyImageContainer

class Repository(private val database: DATABASE) {
    suspend fun refreshDATABASE(){
        withContext(Dispatchers.IO) {
            val listOfAsteroids = AsteroidsApi.retrofitService.getAsteroids().toString()
            val listOfAdaptedAsteroids = parseAsteroidsJsonResult(JSONObject(listOfAsteroids))
            database.asteroidsDao.insertAllAsteroids(*listOfAdaptedAsteroids.asDatabaseModel())
            val dailyImage = ImageApi.retrofitService.getImage()
            dailyImageContainer.dailyImages =dailyImage
            database.imageDao.insertImage(*dailyImageContainer.asDatabaseModel())
        }
    }
}