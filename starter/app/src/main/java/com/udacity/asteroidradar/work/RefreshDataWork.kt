package com.udacity.asteroidradar.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.Repository
import retrofit2.HttpException

/** simple work planification*/

class RefreshDataWorker(appContext: Context, params: WorkerParameters):
        CoroutineWorker(appContext,params){

    companion object{
        const val WORK_NAME = "RefreshDataWorker"
    }


    override suspend fun doWork(): Result {
        val database = getDatabase(applicationContext)
        val repository = Repository(database)

        return try{
            repository.refreshDATABASE()
            Result.success()
        }catch(exception: HttpException){
            Result.retry()
        }
    }
}