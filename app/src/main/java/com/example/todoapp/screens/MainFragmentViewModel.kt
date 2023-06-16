package com.example.todoapp.screens

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.todoapp.model.TodoItem
import com.example.todoapp.model.TodoItemsRepository

class MainFragmentViewModel:ViewModel() {

    var completedTask: MutableLiveData<Int>? = MutableLiveData()
    private val todoItemsRepository = TodoItemsRepository()

    init {
        countCompletedTask(todoItemsRepository.getTodoItems())
    }

    fun countCompletedTask(list: List<TodoItem>){
        var count = 0
        for(element in list){
            if (element.flag == true) count++
        }
        completedTask?.postValue(count)

    }


}