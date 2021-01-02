package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
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

            val asteroidsList = AsteroidsApi.RETROFIT_SERVICEASTEROID.getAsteroids(
                    getNextSevenDaysFormattedDates().first(),
                    getNextSevenDaysFormattedDates().last(),
                    "lao4UxePXSg8NRWBiVOgmvOW2LQ7tl6MWArILLuP").await()
            val asteroidsParsed = parseAsteroidsJsonResult(JSONObject(asteroidsList))

            database.asteroidsDao.insertAllAsteroids(*asteroidsParsed.asDatabaseModel())
            database.asteroidsDao.deleteOldsAsteroids(getNextSevenDaysFormattedDates().first())
            val a = dailyImageResponse.asDatabaseModel(dailyImageResponse)
            println("${a.explanation} prueba del metodo")
            database.dailyImageDao.insertImage(a)
            println("${database.dailyImageDao.getImage().value?.date} obtenida de la db")
        }
    }
}
