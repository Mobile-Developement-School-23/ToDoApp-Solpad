package com.example.todoapp.screens.adding

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.db.TodoItemsDatabase
import com.example.todoapp.model.TodoItem
import com.example.todoapp.repository.TodoItemsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddingFragmentViewModel(application: Application) :AndroidViewModel(application) {

    private val todoItemsRepository = TodoItemsRepository(getApplication<Application>().applicationContext,
        TodoItemsDatabase(application))

    fun addItemToList(todoItem: TodoItem) = viewModelScope.launch(Dispatchers.IO) {
        todoItemsRepository.addTodoItems(getApplication<Application>().applicationContext, todoItem, null)
    }


    fun editItemToList(todoItem: TodoItem) = viewModelScope.launch(Dispatchers.IO)  {
        todoItemsRepository.editTodoItem(getApplication<Application>().applicationContext,todoItem)
    }

    fun deleteTodoItem(todoItem: TodoItem,id: String) = viewModelScope.launch(Dispatchers.IO)  {
        todoItemsRepository.deleteTodoItem(todoItem, id)

    }

}