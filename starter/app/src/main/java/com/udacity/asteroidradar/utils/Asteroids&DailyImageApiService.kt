package com.udacity.asteroidradar.utils

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants.ASTEROIDSAPI_URL
import com.udacity.asteroidradar.Constants.IMAGEAPI_KEY
import com.udacity.asteroidradar.Constants.IMAGEAPI_URL
import com.udacity.asteroidradar.objects.dataTransferObjects.NetworkDailyImage
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface AppApiService {
    @GET("neo/rest/v1/feed")
    fun getAsteroids(
            @Query("start_date") startDate: String,
            @Query("end_date") endDate: String,
            @Query("api_key") apiKey: String
    ): Call<String>

    @GET(IMAGEAPI_KEY)
    fun getImage(): Call<NetworkDailyImage>

}

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

object AsteroidsApi{
    private val retrofitAsteroid = Retrofit.Builder()
            .addConverterFactory(ScalarsConverterFactory.create())
            .baseUrl(ASTEROIDSAPI_URL)
            .build()

    val RETROFIT_SERVICEASTEROID: AppApiService by lazy{
        retrofitAsteroid.create(AppApiService::class.java)
    }
}

object DailyImageApi{
    private val retrofitImage = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(IMAGEAPI_URL)
            .build()

    val RETROFIT_SERVICEDAILYIMAGE: AppApiService by lazy{
        retrofitImage.create(AppApiService::class.java)
    }
}
