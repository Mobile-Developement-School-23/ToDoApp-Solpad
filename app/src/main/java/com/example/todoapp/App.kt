package com.example.todoapp

import android.app.Application
import android.util.Log
import androidx.work.*
import com.example.todoapp.network.WorkManagerDatabase
import java.util.concurrent.TimeUnit


class App : Application() {
    override fun onCreate() {
        super.onCreate()
        setBackgroundWorker()
    }

    fun setBackgroundWorker(){

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val request = PeriodicWorkRequestBuilder<WorkManagerDatabase>(8,TimeUnit.HOURS)
            .setConstraints(constraints).build()
        val workManagerDatabase = WorkManager.getInstance(applicationContext)
        workManagerDatabase.enqueueUniquePeriodicWork(
                "hour_worker",
                ExistingPeriodicWorkPolicy.KEEP,
                request
        )

    }
}