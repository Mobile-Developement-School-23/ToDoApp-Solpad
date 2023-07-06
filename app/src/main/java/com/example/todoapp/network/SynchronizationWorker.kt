package com.example.todoapp.network

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.todoapp.Application
import com.example.todoapp.repository.TodoItemsRepository
import javax.inject.Inject

class SynchronizationWorker(appContext: Context, params: WorkerParameters ): CoroutineWorker(appContext, params) {


    @Inject
    lateinit var repository:TodoItemsRepository
    override suspend fun doWork(): Result =
        try {
            repository.getTodoItemsNetwork()
            Result.success()
        } catch (e:InterruptedException){
            Result.failure()
        }

}