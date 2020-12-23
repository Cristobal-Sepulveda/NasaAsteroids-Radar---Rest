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
import com.udacity.asteroidradar.database.databaseObjects.DatabaseAsteroidEntity
import com.udacity.asteroidradar.database.databaseObjects.DatabaseDailyImageEntity

@Dao
interface AsteroidsDao {
    @Query("select * from databaseasteroidentity")
    fun getAsteroids(): LiveData<List<DatabaseAsteroidEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllAsteroids(vararg videos: DatabaseAsteroidEntity)
}

@Dao
interface DailyImageDao{
    @Query("select * from databasedailyimageentity")
    fun getImage(): LiveData<List<DatabaseDailyImageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertImage(vararg videos: DatabaseDailyImageEntity)
}

@Database(entities = [AsteroidsDao::class, DailyImageDao::class], version = 1, exportSchema = false)
abstract class DATABASE: RoomDatabase(){
    abstract val asteroidsDao: AsteroidsDao
    abstract val imageDao: DailyImageDao
}

private lateinit var INSTANCE: DATABASE
fun getDatabase(context:Context): DATABASE{
    synchronized(DATABASE::class.java){
        if(!::INSTANCE.isInitialized){
            INSTANCE = Room.databaseBuilder(context.applicationContext,
            DATABASE::class.java,
            "DATABASE").build()
        }
    }
    return INSTANCE
}