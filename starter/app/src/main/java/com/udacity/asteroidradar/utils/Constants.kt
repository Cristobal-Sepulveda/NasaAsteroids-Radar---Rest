package com.udacity.asteroidradar.utils

import com.udacity.asteroidradar.BuildConfig


/** Usefull constants to use everywhere */

object Constants {
    const val API_QUERY_DATE_FORMAT = "yyyy-MM-dd"
    const val DEFAULT_END_DATE_DAYS = 7
    const val ASTEROIDSAPI_URL = "https://api.nasa.gov/"
    const val IMAGEAPI_URL = "https://api.nasa.gov/planetary/"

    /** You must have your own API Key to test this app, you can get it in the web site:
     * ASTEROID_API_KEY & DAILYIMAGE_API_KEY  = https://api.nasa.gov/
     *
     * Then, when you have it, put it in your own gradle.properties file. (you can find it
     * by press 2 times shift and then search it in the bar)
     *  */
    const val ASTEROIDS_API_KEY = BuildConfig.ASTEROIDS_API_KEY
    const val DAILYIMAGE_API_KEY = BuildConfig.DAILYIMAGE_API_KEY

}

