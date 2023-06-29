package com.example.todoapp.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.todoapp.db.model.TodoItem

@Dao
interface ItemRoomDao {

    @Query("SELECT * FROM todoitem")
    suspend fun getAll():List<TodoItem>

    @Insert
    suspend fun addTodoItem(todoItem: TodoItem)
}