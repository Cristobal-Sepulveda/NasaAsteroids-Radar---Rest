package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.database.DATABASE
import com.udacity.asteroidradar.database.todayAsteroids
import com.udacity.asteroidradar.network.*
import com.udacity.asteroidradar.objects.dataTransferObjects.*
import com.udacity.asteroidradar.objects.databaseObjects.DatabaseDailyImageEntity
import com.udacity.asteroidradar.objects.databaseObjects.asDomainModel
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

    var todayAsteroids= Transformations.map(database.asteroidsDao.todayAsteroids()){
        it.asDomainModel()
    }

    suspend fun refreshDATABASE(){
        withContext(Dispatchers.IO) {
            val dailyImageResponse = DailyImageApi.RETROFIT_SERVICEDAILYIMAGE.getImage().await()
            val asteroidsList = AsteroidsApi.RETROFIT_SERVICEASTEROID.getAsteroids(
                    getNextSevenDaysFormattedDates().first(),
                    getNextSevenDaysFormattedDates().last(),
                    "lao4UxePXSg8NRWBiVOgmvOW2LQ7tl6MWArILLuP").await()
            val asteroidsParsed = parseAsteroidsJsonResult(JSONObject(asteroidsList))
            database.asteroidsDao.insertAllAsteroids(*asteroidsParsed.asDatabaseModel())
            database.asteroidsDao.deleteOldsAsteroids(getNextSevenDaysFormattedDates().first())
            database.dailyImageDao.insertImage(dailyImageResponse.asDatabaseModel(dailyImageResponse))
        }
    }
}
