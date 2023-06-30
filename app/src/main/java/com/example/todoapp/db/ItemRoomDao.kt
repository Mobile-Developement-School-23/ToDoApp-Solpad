package com.example.todoapp.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.todoapp.model.TodoItem

@Dao
interface ItemRoomDao {

    @Query("SELECT * FROM todoitem")
    suspend fun getAll():List<TodoItem>

    @Insert
    suspend fun addTodoItem(todoItem: TodoItem)

    @Delete
    suspend fun deleteTodoItem(todoItem: TodoItem)

    @Update
    suspend fun editTodoItem(todoItem: TodoItem)

}