package com.example.todoapp.screens.adding

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.db.TodoItemsDatabase
import com.example.todoapp.model.TodoItem
import com.example.todoapp.repository.TodoItemsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddingFragmentViewModel(private val todoItemsRepository: TodoItemsRepository) : ViewModel() {

    fun addItemToList(todoItem: TodoItem) = viewModelScope.launch(Dispatchers.IO) {
        todoItemsRepository.addTodoItems(todoItem, null)
    }


    fun editItemToList(todoItem: TodoItem) = viewModelScope.launch(Dispatchers.IO) {
        todoItemsRepository.editTodoItem(todoItem)
    }

    fun deleteTodoItem(todoItem: TodoItem, id: String) = viewModelScope.launch(Dispatchers.IO) {
        todoItemsRepository.deleteTodoItem(todoItem, id)

    }

}