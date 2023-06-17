package com.example.todoapp.screens.main

import android.util.Log
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

    fun deleteItemToList(item: TodoItem) = viewModelScope.launch {
        todoItemsRepository.deleteTodoItems(item)

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