package com.example.todoapp.network

import android.app.Application
import android.content.Context
import android.util.Log
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

    suspend fun synchronizationData(todoItemsNetwork: List<TodoItem>){

        val revisionNetwork = RevisionService(context).getRevisionNetwork()
        val revisionDatabase = RevisionService(context).getRevisionDatabase()

        if (revisionNetwork > revisionDatabase) {
            synchronizationDatabase(todoItemsNetwork, database.getDao().getAll())
        } else {
            synchronizationNetwork(database.getDao().getAll())
        }
    }

    suspend fun synchronizationDatabase(todoItemsDatabase: List<TodoItem>, todoItemsNetwork: List<TodoItem>) {
        if(todoItemsDatabase.isNotEmpty()) database.getDao().deleteAll()
        database.getDao().addAllTodoItems(todoItemsNetwork)

    }
    suspend fun synchronizationNetwork(todoItemsDatabase: List<TodoItem>) = withContext(Dispatchers.IO){
        todoItemsRepository.patchTodoItemNetwork(context,todoItemsDatabase)
    }

}