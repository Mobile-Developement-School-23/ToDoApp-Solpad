package com.example.todoapp.screens.main

import android.app.Application
import android.net.ConnectivityManager
import android.net.Network
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.db.TodoItemsDatabase
import com.example.todoapp.model.Resource
import com.example.todoapp.model.TodoItem
import com.example.todoapp.repository.TodoItemsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainFragmentViewModel(
    private val todoItemsRepository: TodoItemsRepository,
    private val connectivityManager: ConnectivityManager
    ) : ViewModel() {

    private val _internetConnection = MutableStateFlow(true)
    val internetConnection = _internetConnection

    private var _todoList = MutableStateFlow(listOf<TodoItem>())
    val todoList = _todoList.asStateFlow()


    init {
        setInternetStatus()
    }
    fun addTodoItem(todoItem: TodoItem) = viewModelScope.launch(Dispatchers.IO) {
        todoItemsRepository.addTodoItems(todoItem, null
        )
    }

    fun deleteTodoItem(item: TodoItem, id: String) = viewModelScope.launch(Dispatchers.IO) {
        todoItemsRepository.deleteTodoItem(item, id)
    }

    fun editTodoItem(todoItem: TodoItem) = viewModelScope.launch(Dispatchers.IO) {
        todoItemsRepository.editTodoItem(todoItem)
    }

    fun getListTodoItems() = viewModelScope.launch(Dispatchers.IO) {
        todoItemsRepository.getTodoItems()

    }


    fun getTodoItemsLiveData(): LiveData<List<TodoItem>> {
        return todoItemsRepository.mTodoItemsLiveData
    }

    fun getResourseLiveData(): LiveData<Resource> {
        return todoItemsRepository.resourseRequest
    }

    fun checkInternetConnection() = todoItemsRepository.checkInternetConnection()


    private fun setInternetStatus(){
        viewModelScope.launch {
            connectivityManager.getInternetStatus().collect(){
                _internetConnection.value = it
            }
        }
    }
    fun ConnectivityManager.getInternetStatus(): Flow<Boolean> = callbackFlow {
        val callback = object : ConnectivityManager.NetworkCallback(){
            override fun onAvailable(network: Network) {
                trySend(true)
            }

            override fun onLost(network: Network) {
                trySend(false)
            }
        }

        registerDefaultNetworkCallback(callback)

        awaitClose { unregisterNetworkCallback(callback) }

    }

}