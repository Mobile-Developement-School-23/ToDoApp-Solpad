package com.example.todoapp.network

import android.app.Application
import android.content.Context
import com.example.todoapp.db.TodoItemsDatabase
import com.example.todoapp.model.TodoItem
import com.example.todoapp.network.model.TodoItemNetwork
import com.example.todoapp.repository.TodoItemsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SynchronizationData(context:Context) {
    private val context = context
    private val database = TodoItemsDatabase(context)
    private val todoItemsRepository = TodoItemsRepository(context,database)

    suspend fun synchronizationData(todoItemsDatabase: List<TodoItem>,todoItemsNetwork: List<TodoItem>){

        synchronizationDatabase(todoItemsDatabase,todoItemsNetwork)
    }

    suspend fun synchronizationDatabase(todoItemsDatabase: List<TodoItem>, todoItemsNetwork: List<TodoItem>) {
        if(todoItemsDatabase.isNotEmpty()) database.getDao().deleteAll()
        database.getDao().addAllTodoItems(todoItemsNetwork)

    }
    suspend fun synchronizationNetwork(todoItemsDatabase: List<TodoItem>) = withContext(Dispatchers.IO){
        todoItemsRepository.patchTodoItemNetwork(context,todoItemsDatabase)
    }

}