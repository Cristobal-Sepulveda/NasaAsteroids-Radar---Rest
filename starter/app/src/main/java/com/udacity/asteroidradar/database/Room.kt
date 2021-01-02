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

package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.udacity.asteroidradar.objects.databaseObjects.DatabaseAsteroidEntity
import com.udacity.asteroidradar.objects.databaseObjects.DatabaseDailyImageEntity
import com.udacity.asteroidradar.utils.getNextSevenDaysFormattedDates
@Dao
interface AsteroidsDao {
    @Query("select * from databaseasteroidentity order by closeApproachDate ASC")
    fun getAsteroids(): LiveData<List<DatabaseAsteroidEntity>>

    @Query("select * from databaseasteroidentity where closeApproachDate = (:today)")
    fun getAsteroidsFromToday(today: String): LiveData<List<DatabaseAsteroidEntity>>

    @Query("delete from databaseasteroidentity where closeApproachDate <(:days)")
    fun deleteOldsAsteroids(days: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllAsteroids(vararg asteroid: DatabaseAsteroidEntity)
}

fun AsteroidsDao.todayAsteroids():LiveData<List<DatabaseAsteroidEntity>>{
    val today = getNextSevenDaysFormattedDates().first()
    return getAsteroidsFromToday(today)
}

@Dao
interface DailyImageDao{
    @Query("select * from databasedailyimageentity")
    fun getImage(): LiveData<DatabaseDailyImageEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertImage(vararg image: DatabaseDailyImageEntity)
}

@Database(entities = [DatabaseAsteroidEntity::class, DatabaseDailyImageEntity::class],
            version = 1,
            exportSchema = false)
abstract class DATABASE: RoomDatabase() {
    abstract val asteroidsDao: AsteroidsDao
    abstract val dailyImageDao: DailyImageDao
}

private lateinit var INSTANCE: DATABASE

fun getDatabase(context: Context): DATABASE {
    synchronized(DATABASE::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                    DATABASE::class.java,
                    "database").build()
        }
    }
    return INSTANCE
}
