package com.example.todoapp.db

import androidx.room.TypeConverter
import com.example.todoapp.model.Importance

class ImportanceConverters {
    @TypeConverter
    fun fromImportance(importance: Importance): String {
        return importance.name
    }

    @TypeConverter
    fun toImportance(name: String): Importance {
        return try {
            Importance.valueOf(name)
        } catch (e: Exception) {
            Importance.BASIC
        }
    }
}