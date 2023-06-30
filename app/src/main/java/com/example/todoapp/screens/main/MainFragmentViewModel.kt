package com.example.todoapp.screens.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.todoapp.App
import com.example.todoapp.db.TodoItemsDatabase
import com.example.todoapp.model.Resourse
import com.example.todoapp.model.TodoItem
import com.example.todoapp.network.model.GetListItemsNetwork
import com.example.todoapp.repository.TodoItemsRepository
import com.example.todoapp.util.CheckInternetConnection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainFragmentViewModel(application: Application) :AndroidViewModel(application) {

    private val todoItemsRepository = TodoItemsRepository(getApplication<Application>().applicationContext,
        TodoItemsDatabase(application))

    fun addTodoItem(todoItem: TodoItem) = viewModelScope.launch(Dispatchers.IO) {
        todoItemsRepository.addTodoItems(getApplication<Application>().applicationContext, todoItem, null)
    }
    fun deleteTodoItem(item: TodoItem,id: String) = viewModelScope.launch(Dispatchers.IO) {
        todoItemsRepository.deleteTodoItem(item,id)
    }

    fun getListTodoItems() = viewModelScope.launch(Dispatchers.IO)  {
        todoItemsRepository.getTodoItems()
    }


    fun getTodoItemsLiveData(): LiveData<List<TodoItem>> {
        return todoItemsRepository.todoItemsLiveData
    }
    fun getResourseLiveData(): LiveData<Resourse> {
        return todoItemsRepository.resourseRequest
    }

    fun checkInternetConnection() = todoItemsRepository.checkInternetConnection(getApplication<Application>().applicationContext)
}