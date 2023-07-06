package com.example.todoapp.network.model

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.example.todoapp.network.SynchronizationWorker
import com.example.todoapp.repository.TodoItemsRepository
import javax.inject.Inject


class SynchronizationWorkerFactory @Inject constructor() : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return when (workerClassName) {
            SynchronizationWorker::class.java.name -> SynchronizationWorker(appContext, workerParameters)
            else -> null
        }
    }
}