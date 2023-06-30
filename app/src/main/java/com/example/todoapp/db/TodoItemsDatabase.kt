package com.example.todoapp.db

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.todoapp.model.TodoItem

@Database(
    entities = [TodoItem::class],
    version = 1
)
abstract class TodoItemsDatabase:RoomDatabase() {

    abstract fun getDao():ItemRoomDao

    companion object {
        @Volatile
        var instance: TodoItemsDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context): TodoItemsDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                TodoItemsDatabase::class.java,
                "item_db.db"
            ).build()
        }
    }
}