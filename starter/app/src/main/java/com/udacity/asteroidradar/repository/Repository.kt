package com.udacity.asteroidradar.repository

import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.utils.Constants
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

    /** as the function name said, this function refresh the database to be updated at the time the
     * user open the app */

        suspend fun refreshDATABASE(){
        withContext(Dispatchers.IO) {

            /** Because the Response from the dailyImage Server is only one object,
             * i save the response as an Data Transfer object, and the table inside the database
             * only contains one object, therefore, when the day change, a new dailyImage is
             * returned by the server, so, i delete the old one in the table
             * before save the new dailyImage*/

            val dailyImageResponse = DailyImageApi.RETROFIT_SERVICE_DAILYIMAGE.getDailyImage().await()
            database.dailyImageDao.deleteOldsAsteroids(getNextSevenDaysFormattedDates().first())
            database.dailyImageDao.insertImage(dailyImageResponse.asDatabaseModel(dailyImageResponse))

            /** Here im receiving the string response from the server, converting it
             * to a list of  Data Transfer Objects, and wrapping them in a NetworkAsteroid
             * Container, to manipulate it later, and im deleting the Asteroids with date
             * before as a  new day*/

            val asteroidsList = AsteroidsApi.RETROFIT_SERVICE_ASTEROID.getAsteroids(
                    getNextSevenDaysFormattedDates().first(),
                    getNextSevenDaysFormattedDates().last(),
                    Constants.ASTEROIDS_API_KEY).await()
            val asteroidsParsed = parseAsteroidsJsonResult(JSONObject(asteroidsList))
            database.asteroidsDao.insertAllAsteroids(*asteroidsParsed.asDatabaseModel())
            database.asteroidsDao.deleteOldsAsteroids(getNextSevenDaysFormattedDates().first())
        }
    }

    /** Here, im getting the data that i will need in the MainViewModel from the Database,
     * but, how i will need it to display in the Ui, the good practices saids that i need
     * to convert the database objects to domain objects.
     */

    val asteroidsListFromDatabase = Transformations.map(database.asteroidsDao.getAsteroids()){
        it.asDomainModel()
    }

    val todayAsteroidsFromDatabase = Transformations.map(database.asteroidsDao.getAsteroidsFromToday(
            getNextSevenDaysFormattedDates().first())){
        it.asDomainModel()
    }

    val dailyImageFromDatabase = database.dailyImageDao.getImage()

    /************************************************************************************/
}
