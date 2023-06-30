package com.example.todoapp.repository

import androidx.lifecycle.LiveData
import com.example.todoapp.model.TodoItem
import com.example.todoapp.network.RevisionService
import com.example.todoapp.network.model.SetItemResponse

class ConverterRepository( private val revisionService: RevisionService ) {

    fun converterAddTodoItem(body: SetItemResponse,itemLiveData: LiveData<List<TodoItem>>): List<TodoItem> {
        revisionService.setRevisionNetwork(body.revision)
        val newItem = body.todoItemNetwork.mapToTodoItem()
        val todoItems = itemLiveData.value
        if (todoItems != null) {
            val newList = todoItems.toMutableList()
            var flag = true
            todoItems.forEachIndexed { index, todoItem ->
                if (newItem.dateChanged < todoItem.dateChanged) {
                    newList.add(index, newItem)
                    flag = false
                    return newList.toList()
                }
            }
            if (flag) {
                newList.add(newItem)
                return newList.toList()
            }
        }
        return emptyList()
    }

    fun converterDeleteTodoItem(body: SetItemResponse,itemLiveData: LiveData<List<TodoItem>>): List<TodoItem> {
        revisionService.setRevisionNetwork(body.revision)
        val deletedItem = body.todoItemNetwork.mapToTodoItem()
        val todoItems = itemLiveData.value
        if (todoItems != null) {
            val newList = todoItems.toMutableList()
            todoItems.forEachIndexed { index, todoItem ->
                if (todoItem.id == deletedItem.id) {
                    newList.removeAt(index)
                    return@forEachIndexed
                }
            }
            return newList.toList()
        } else return emptyList()
    }

    fun converterEditTodoItem(body: SetItemResponse,itemLiveData: LiveData<List<TodoItem>>): List<TodoItem> {
        revisionService.setRevisionNetwork(body.revision)
        val editItem = body.todoItemNetwork.mapToTodoItem()
        val todoItems = itemLiveData.value
        var newList = todoItems?.toMutableList()
        if (todoItems != null) {
            todoItems.forEachIndexed { index, todoItem ->
                if (todoItem.id == editItem.id) {
                    newList?.set(index, editItem)
                    return@forEachIndexed
                }
            }
            return newList?.toList() ?: emptyList()
        } else return emptyList()
    }


}