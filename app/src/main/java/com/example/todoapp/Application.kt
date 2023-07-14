package com.example.todoapp

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.UriPermission
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat
import androidx.work.*
import com.example.todoapp.di.app.AppComponent
import com.example.todoapp.di.app.DaggerAppComponent
import com.example.todoapp.network.AlarmReceiver
import com.example.todoapp.network.SynchronizationWorker
import com.example.todoapp.repository.TodoItemsRepository
import java.util.Calendar
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class Application : Application() {
    open lateinit var appComponent:AppComponent

    @Inject
    lateinit var todoItemsRepository: TodoItemsRepository
    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.factory().create(this)
        appComponent.inject(this)
        setBackgroundWorker()
    }

    fun setBackgroundWorker(){

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val request = PeriodicWorkRequestBuilder<SynchronizationWorker>(8,TimeUnit.HOURS)
            .setConstraints(constraints).build()
        val workManagerDatabase = WorkManager.getInstance(applicationContext)
        workManagerDatabase.enqueueUniquePeriodicWork(
                "hour_worker",
                ExistingPeriodicWorkPolicy.KEEP,
                request
        )

    }


}