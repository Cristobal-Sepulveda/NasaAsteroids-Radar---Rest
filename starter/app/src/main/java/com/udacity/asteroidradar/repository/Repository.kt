package com.udacity.asteroidradar.repository

import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.database.DATABASE
import com.udacity.asteroidradar.objects.dataTransferObjects.*
import com.udacity.asteroidradar.objects.databaseObjects.asDomainModel
import com.udacity.asteroidradar.utils.AsteroidsApi
import com.udacity.asteroidradar.utils.DailyImageApi
import com.udacity.asteroidradar.utils.getNextSevenDaysFormattedDates
import com.udacity.asteroidradar.utils.parseAsteroidsJsonResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.await

class Repository(private val database: DATABASE) {

    val asteroidsFromDatabase = Transformations.map(database.asteroidsDao.getAsteroids()){
                it.asDomainModel()
            }
    var todayAsteroids= Transformations.map(database.asteroidsDao.getAsteroidsFromToday(
            getNextSevenDaysFormattedDates().first())){
        it.asDomainModel()
    }

    val dailyImageFromDatabase= database.dailyImageDao.getImage()

    suspend fun refreshDATABASE(){
        withContext(Dispatchers.IO) {
            val dailyImageFromDatabase= database.dailyImageDao.getImage().value
            val dailyImageResponse = DailyImageApi.RETROFIT_SERVICEDAILYIMAGE.getImage().await()
            if(dailyImageFromDatabase != null &&
                    dailyImageFromDatabase.date < getNextSevenDaysFormattedDates().first()){
                database.dailyImageDao.deleteOldsAsteroids(getNextSevenDaysFormattedDates().first())
            }
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
