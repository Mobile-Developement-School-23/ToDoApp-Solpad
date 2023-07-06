package com.example.todoapp.domain

import android.content.Context
import com.example.todoapp.model.TodoItem
import com.example.todoapp.network.SynchronizationData
import com.example.todoapp.repository.TodoItemsRepository
import com.example.todoapp.util.CheckInternetConnection
import javax.inject.Inject

class SetItemInteractor @Inject constructor(
    val todoItemsRepository: TodoItemsRepository,
    val synchronizationData: SynchronizationData,
    val checkInternetConnection: CheckInternetConnection
){

    suspend fun addTodoItems( todoItem: TodoItem, id: String?) {
        if (checkInternetConnection.checkInternet()) {
            todoItemsRepository.addTodoItemNetwork(todoItem, id)
            todoItemsRepository.addTodoItemDatabase(todoItem,id)
        } else {
            todoItemsRepository.addTodoItemDatabase(todoItem,id)
        }
    }
}