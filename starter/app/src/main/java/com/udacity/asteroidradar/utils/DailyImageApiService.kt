package com.udacity.asteroidradar.utils

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.objects.dataTransferObjects.NetworkDailyImage
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

/** API to communicate the server with this app, the response is converted in
 *  a DataTransfer Object */

interface DailyImageApiService{
    @GET(Constants.IMAGEAPI_KEY)
    fun getDailyImage(): Call<NetworkDailyImage>
}

private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

object DailyImageApi{
    private val retrofitDailyImage = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(Constants.IMAGEAPI_URL)
            .build()

    val RETROFIT_SERVICE_DAILYIMAGE: DailyImageApiService by lazy{
        retrofitDailyImage.create(DailyImageApiService::class.java)
    }
}