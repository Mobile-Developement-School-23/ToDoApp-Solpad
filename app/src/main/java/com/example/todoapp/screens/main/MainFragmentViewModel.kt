package com.example.todoapp.screens.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.todoapp.model.TodoItem
import com.example.todoapp.repository.TodoItemsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainFragmentViewModel(application: Application) :AndroidViewModel(application) {

    private val todoItemsRepository = TodoItemsRepository(getApplication<Application>().applicationContext)

    fun addItemToList(todoItem: TodoItem) = viewModelScope.launch(Dispatchers.IO) {
        todoItemsRepository.addTodoItemNetwork(getApplication<Application>().applicationContext, todoItem, null)
    }
    fun deleteTodoItem(id: String) = viewModelScope.launch(Dispatchers.IO) {
        todoItemsRepository.deleteTodoItemNetwork(getApplication<Application>().applicationContext, id)
    }
    fun getTodoItemsLiveData(): LiveData<List<TodoItem>> {
        return todoItemsRepository.todoItemsLiveData
    }
    fun getListTodoItems() = viewModelScope.launch(Dispatchers.IO)  {
        todoItemsRepository.getTodoItemsNetwork(getApplication<Application>().applicationContext)
    }

}