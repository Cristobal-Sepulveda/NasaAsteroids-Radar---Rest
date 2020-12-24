package com.udacity.asteroidradar.network
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants.ASTEROIDSAPI_KEY
import com.udacity.asteroidradar.Constants.ASTEROIDSAPI_URL
import com.udacity.asteroidradar.Constants.IMAGEAPI_KEY
import com.udacity.asteroidradar.Constants.IMAGEAPI_URL
import com.udacity.asteroidradar.objects.domainObjects.DailyImage
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofitAsteroid = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(ASTEROIDSAPI_URL)
    .build()

private val retrofitImage = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(IMAGEAPI_URL)
    .build()

interface AsteroidsApiService {
    @GET(ASTEROIDSAPI_KEY)
    fun getAsteroids():
            Call<String>
    @GET(IMAGEAPI_KEY)
    fun getImage():
            Call<DailyImage>
}

object AsteroidsApi{
    val retrofitService: AsteroidsApiService by lazy{
        retrofitAsteroid.create(AsteroidsApiService::class.java)
    }
}
object ImageApi{
    val retrofitService: AsteroidsApiService by lazy{
        retrofitImage.create(AsteroidsApiService::class.java)
    }
}
