package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.database.DATABASE
import com.udacity.asteroidradar.database.todayAsteroids
import com.udacity.asteroidradar.network.*
import com.udacity.asteroidradar.objects.dataTransferObjects.*
import com.udacity.asteroidradar.objects.databaseObjects.asDomainModel
import com.udacity.asteroidradar.objects.domainObjects.DailyImage
import com.udacity.asteroidradar.utils.AsteroidsApi
import com.udacity.asteroidradar.utils.DailyImageApi
import com.udacity.asteroidradar.utils.getNextSevenDaysFormattedDates
import com.udacity.asteroidradar.utils.parseAsteroidsJsonResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.await

class Repository(private val database: DATABASE) {

    val asteroidsFromDatabase:LiveData<List<Asteroid>> =
            Transformations.map(database.asteroidsDao.getAsteroids()){
                it.asDomainModel()
            }
    var todayAsteroids= Transformations.map(database.asteroidsDao.todayAsteroids()){
        it.asDomainModel()
    }

    val dailyImageFromDatabase= database.dailyImageDao.getImage()


    suspend fun refreshDATABASE(){
        withContext(Dispatchers.IO) {
                val dailyImageResponse = DailyImageApi.RETROFIT_SERVICEDAILYIMAGE.getImage().await()
                database.dailyImageDao.insertImage(dailyImageResponse.asDatabaseModel(dailyImageResponse))

                val asteroidsList = AsteroidsApi.RETROFIT_SERVICEASTEROID.getAsteroids(
                    getNextSevenDaysFormattedDates().first(),
                    getNextSevenDaysFormattedDates().last(),
                    Constants.ASTEROIDSAPI_KEY).await()
                val asteroidsParsed = parseAsteroidsJsonResult(JSONObject(asteroidsList))
                database.asteroidsDao.insertAllAsteroids(*asteroidsParsed.asDatabaseModel())
                database.asteroidsDao.deleteOldsAsteroids(getNextSevenDaysFormattedDates().first())

            }
        }
    }
