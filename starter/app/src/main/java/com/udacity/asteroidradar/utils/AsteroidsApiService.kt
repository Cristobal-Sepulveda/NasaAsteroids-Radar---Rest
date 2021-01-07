package com.udacity.asteroidradar.utils

import com.udacity.asteroidradar.utils.Constants.ASTEROIDSAPI_URL
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/** API to communicate the server with this app, the response is a String,
 *  and then i convert it to a NetworkAsteroidContainer in the method
 *  parseAsteroidsJsonResults in NetworkUtils.kt*/

interface AsteroidApiService {
    @GET("neo/rest/v1/feed")
    fun getAsteroids(
            @Query("start_date") startDate: String,
            @Query("end_date") endDate: String,
            @Query("api_key") apiKey: String
    ): Call<String>
}

object AsteroidsApi{
    private val retrofitAsteroid = Retrofit.Builder()
            .addConverterFactory(ScalarsConverterFactory.create())
            .baseUrl(ASTEROIDSAPI_URL)
            .build()

    val RETROFIT_SERVICE_ASTEROID: AsteroidApiService by lazy{
        retrofitAsteroid.create(AsteroidApiService::class.java)
    }
}
