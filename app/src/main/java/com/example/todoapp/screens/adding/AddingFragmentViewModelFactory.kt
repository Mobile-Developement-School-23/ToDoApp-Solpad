package com.example.todoapp.screens.adding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todoapp.di.app.AppScope
import com.example.todoapp.repository.TodoItemsRepository
import javax.inject.Inject

@AppScope
class AddingFragmentViewModelFactory @Inject constructor(
    private val repository: TodoItemsRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AddingFragmentViewModel(repository) as T
    }
}