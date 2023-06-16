package com.example.todoapp.screens

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.model.TodoItem
import com.example.todoapp.model.TodoItemsRepository
import kotlinx.coroutines.launch

class AddingFragmentViewModel:ViewModel() {

    private val todoItemsRepository = TodoItemsRepository()

    fun addItemToList(item: TodoItem) = viewModelScope.launch {
        todoItemsRepository.setTodoItems(item)
    }

    fun deleteItemToList(item: TodoItem) = viewModelScope.launch {
        todoItemsRepository.deleteTodoItems(item)
    }

    fun changeItemToList(item: TodoItem) = viewModelScope.launch {
        todoItemsRepository.changeTodoItems(item)
    }
}