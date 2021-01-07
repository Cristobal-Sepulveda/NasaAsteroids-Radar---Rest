package com.udacity.asteroidradar.utils

import com.udacity.asteroidradar.BuildConfig


/** Usefull constants to use everywhere */

object Constants {
    const val API_QUERY_DATE_FORMAT = "yyyy-MM-dd"
    const val DEFAULT_END_DATE_DAYS = 7
    const val ASTEROIDSAPI_URL = "https://api.nasa.gov/"
    const val IMAGEAPI_URL = "https://api.nasa.gov/planetary/"
    const val ASTEROIDS_API_KEY = BuildConfig.ASTEROIDS_API_KEY
    const val DAILYIMAGE_API_KEY = BuildConfig.DAILYIMAGE_API_KEY

}

