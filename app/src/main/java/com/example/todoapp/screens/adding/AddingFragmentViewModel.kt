package com.example.todoapp.screens.adding

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.model.TodoItem
import com.example.todoapp.repository.TodoItemsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddingFragmentViewModel(application: Application) :AndroidViewModel(application) {

    private val todoItemsRepository = TodoItemsRepository(getApplication<Application>().applicationContext)

    fun addItemToList(todoItem: TodoItem) = viewModelScope.launch(Dispatchers.IO) {
        todoItemsRepository.addTodoItemNetwork(getApplication<Application>().applicationContext, todoItem, null)
    }


    fun editItemToList(item: TodoItem) = viewModelScope.launch(Dispatchers.IO)  {
        todoItemsRepository.editTodoItemNetwork(getApplication<Application>().applicationContext,item)
    }

    fun deleteTodoItem(id: String) = viewModelScope.launch(Dispatchers.IO)  {
        todoItemsRepository.deleteTodoItemNetwork(getApplication<Application>().applicationContext, id)

    }

}