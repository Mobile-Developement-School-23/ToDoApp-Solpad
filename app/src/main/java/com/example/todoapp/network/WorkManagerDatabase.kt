package com.example.todoapp.network

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.todoapp.db.TodoItemsDatabase
import com.example.todoapp.repository.TodoItemsRepository

class WorkManagerDatabase(appContext: Context, params: WorkerParameters ): CoroutineWorker(appContext, params) {
    var context = appContext
    override suspend fun doWork(): Result =
        try {
            val repository = TodoItemsRepository(context, TodoItemsDatabase(context))
            repository.getTodoItemsNetwork(context)
            Result.success()
        } catch (e:InterruptedException){
            Result.failure()
        }


}