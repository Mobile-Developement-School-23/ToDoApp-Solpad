package com.example.todoapp.screens.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.model.TodoItem
import com.example.todoapp.model.TodoItemsRepository
import kotlinx.coroutines.launch

class MainFragmentViewModel:ViewModel() {

    var completedTask: MutableLiveData<Int>? = MutableLiveData()
    private val todoItemsRepository = TodoItemsRepository()

    init {
        countCompletedTask(todoItemsRepository.getTodoItems())
    }

    fun deleteItemFromList(item: TodoItem) = viewModelScope.launch {
        todoItemsRepository.deleteTodoItems(item)

    }
    fun addItemToList(item: TodoItem) = viewModelScope.launch {
        todoItemsRepository.setTodoItems(item)
    }

    // подсчет выполеных тасок
    fun countCompletedTask(list: List<TodoItem>){
        var count = 0
        for(element in list){
            if (element.flag == true) count++
        }
        completedTask?.postValue(count)

    }


}