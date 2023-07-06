package com.example.todoapp.di.data

import android.content.Context
import androidx.room.Room
import com.example.todoapp.db.ItemRoomDao
import com.example.todoapp.db.TodoItemsDatabase
import com.example.todoapp.di.app.AppScope
import dagger.Module
import dagger.Provides

@Module
class DatabaseModule {

    @Provides
    @AppScope
    fun provideDatabase(context: Context): TodoItemsDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            TodoItemsDatabase::class.java,
            "item_db.db"
        ).build()
    }

    @Provides
    @AppScope
     fun provideDatabaseDao(database: TodoItemsDatabase): ItemRoomDao {
        return database.getDao()
    }

}