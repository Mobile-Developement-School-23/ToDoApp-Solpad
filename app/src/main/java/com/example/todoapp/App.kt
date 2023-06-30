package com.example.todoapp

import android.app.Application
import android.util.Log
import androidx.room.Room.databaseBuilder
import com.example.todoapp.db.TodoItemsDatabase


class App : Application() {
    override fun onCreate() {
        super.onCreate()

    }
}