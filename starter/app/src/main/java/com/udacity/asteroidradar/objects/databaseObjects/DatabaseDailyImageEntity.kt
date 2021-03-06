package com.udacity.asteroidradar.objects.databaseObjects

import androidx.lifecycle.LiveData
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.udacity.asteroidradar.objects.domainObjects.DailyImage

@Entity
data class DatabaseDailyImageEntity(
    @PrimaryKey
    val date: String,
    val explanation: String,
    val hdurl: String,
    @Json(name = "media_type") val mediaType: String,
    @Json(name = "service_version")val serviceVersion: String,
    val title: String,
    val url: String)

//Add an extension function which converts from database objects to domain objects:
fun DatabaseDailyImageEntity.asDomainModel(databaseDailyImageEntity: DatabaseDailyImageEntity): DailyImage{
    return DailyImage(
            date = databaseDailyImageEntity.date,
            explanation = databaseDailyImageEntity.explanation,
            hdurl = databaseDailyImageEntity.hdurl,
            mediaType = databaseDailyImageEntity.mediaType,
            serviceVersion = databaseDailyImageEntity.serviceVersion,
            title = databaseDailyImageEntity.title,
            url = databaseDailyImageEntity.url
        )
}