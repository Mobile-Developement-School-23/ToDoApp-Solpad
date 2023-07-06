package com.example.todoapp.network

import android.content.Context
import com.example.todoapp.db.ItemRoomDao
import com.example.todoapp.model.TodoItem
import com.example.todoapp.repository.TodoItemsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SynchronizationData @Inject constructor(
    val todoApiDatabase : ItemRoomDao,
    val revisionService: RevisionService
    ) {





}