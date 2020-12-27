package com.udacity.asteroidradar.network

import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.objects.dataTransferObjects.NetworkAsteroid
import com.udacity.asteroidradar.objects.dataTransferObjects.NetworkAsteroidsContainer
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
fun parseAsteroidsJsonResult(jsonResult: JSONObject): NetworkAsteroidsContainer {
    val nearEarthObjectsJson = jsonResult.getJSONObject("near_earth_objects")
    println(nearEarthObjectsJson)
    val asteroidList = ArrayList<NetworkAsteroid>()
    var x = 0
    val nextSevenDaysFormattedDates = getNextSevenDaysFormattedDates()
    println(nextSevenDaysFormattedDates)
    for (formattedDate in nextSevenDaysFormattedDates) {
        val dateAsteroidJsonArray = nearEarthObjectsJson.getJSONArray(formattedDate)
        for (i in 0 until dateAsteroidJsonArray.length()) {
            val asteroidJson = dateAsteroidJsonArray.getJSONObject(i)
            val id = asteroidJson.getLong("id")
            val codename = asteroidJson.getString("name")
            val absoluteMagnitude = asteroidJson.getDouble("absolute_magnitude_h")
            val estimatedDiameter = asteroidJson.getJSONObject("estimated_diameter")
                .getJSONObject("kilometers").getDouble("estimated_diameter_max")
            val closeApproachData = asteroidJson
                .getJSONArray("close_approach_data").getJSONObject(0)
            val relativeVelocity = closeApproachData.getJSONObject("relative_velocity")
                .getDouble("kilometers_per_second")
            val distanceFromEarth = closeApproachData.getJSONObject("miss_distance")
                .getDouble("astronomical")
            val isPotentiallyHazardous = asteroidJson
                .getBoolean("is_potentially_hazardous_asteroid")
            val asteroid = NetworkAsteroid(id, codename, formattedDate, absoluteMagnitude,
                estimatedDiameter, relativeVelocity, distanceFromEarth, isPotentiallyHazardous)
            asteroidList.add(asteroid)
            //println("$x \n $asteroid \n")
            x++
        }
    }
    var asteroidListContainer = NetworkAsteroidsContainer(asteroidList)
    println("${asteroidList.size} asteroid's detected")
    return asteroidListContainer
}

    fun getNextSevenDaysFormattedDates(): ArrayList<String> {
    val formattedDateList = ArrayList<String>()
    val calendar = Calendar.getInstance()
    for (i in 0..Constants.DEFAULT_END_DATE_DAYS) {
        val currentTime = calendar.time
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        formattedDateList.add(dateFormat.format(currentTime))
        calendar.add(Calendar.DAY_OF_YEAR, 1)
    }
    return formattedDateList
}


