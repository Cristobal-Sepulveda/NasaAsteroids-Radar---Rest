package com.udacity.asteroidradar.network

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Asteroid(val id: Long,
                    @Json(name= "name")val codename: String,
                    @Json(name= "close_approach_data")val closeApproachDate: String,
                    @Json(name= "absolute_magnitude")val absoluteMagnitude: Double,
                    @Json(name= "estimated_diameter")val estimatedDiameter: Double,
                    @Json(name= "relative_velocity")val relativeVelocity: Double,
                    val distanceFromEarth: Double,
                    val isPotentiallyHazardous: Boolean) : Parcelable