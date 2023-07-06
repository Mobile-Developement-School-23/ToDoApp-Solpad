package com.example.todoapp

import android.app.Application
import androidx.work.*
import com.example.todoapp.di.app.AppComponent
import com.example.todoapp.di.app.DaggerAppComponent
import com.example.todoapp.network.SynchronizationWorker
import com.example.todoapp.repository.TodoItemsRepository
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