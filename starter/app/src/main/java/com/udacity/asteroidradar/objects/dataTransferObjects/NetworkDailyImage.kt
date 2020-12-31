/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.udacity.asteroidradar.objects.dataTransferObjects

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.udacity.asteroidradar.objects.databaseObjects.DatabaseDailyImageEntity
import com.udacity.asteroidradar.objects.domainObjects.DailyImage

/**
 * DataTransferObjects go in this file. These are responsible for parsing responses from the server
 * or formatting objects to send to the server. You should convert these to domain objects before
 * using them.
 */

/**
 * VideoHolder holds a list of Videos.
 *
 * This is to parse first level of our network result which looks like
 *
 * {
 *   "videos": []
 * }
 */
/**
 * Videos represent a devbyte that can be played.
 */
@JsonClass(generateAdapter = true)
data class NetworkDailyImage(
    val date: String,
    val explanation: String,
    val hdurl: String,
    @Json(name = "media_type") val mediaType: String,
    @Json(name = "service_version")val serviceVersion: String,
    val title: String,
    val url: String)

/**
 * Convert Network results to domain & database objects
 */
fun NetworkDailyImage.asDomainModel(networkDailyImage: NetworkDailyImage): List<DailyImage> {
    return DailyImage(networkDailyImage.date,
    networkDailyImage.explanation,
    networkDailyImage.hdurl,
    networkDailyImage.mediaType,
    networkDailyImage.serviceVersion,
    networkDailyImage.title,
    networkDailyImage.url)
}

fun NetworkDailyImage.asDatabaseModel(networkDailyImage: NetworkDailyImage): DatabaseDailyImageEntity {
    return DatabaseDailyImageEntity(networkDailyImage.date,
            networkDailyImage.explanation,
            networkDailyImage.hdurl,
            networkDailyImage.mediaType,
            networkDailyImage.serviceVersion,
            networkDailyImage.title,
            networkDailyImage.url)
}
