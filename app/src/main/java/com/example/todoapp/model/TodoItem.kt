package com.example.todoapp.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.todoapp.db.ImportanceConverters
import kotlinx.parcelize.Parcelize


@Entity
@TypeConverters(ImportanceConverters::class)
@Parcelize
data class TodoItem(
    @PrimaryKey
    var id:String,
    var content:String,
    var importance: Importance,
    var flag:Boolean,
    var dateCreated: Long,
    var dateChanged: Long=0L,
    var deadline: Long = 0L,
): Parcelable {
}