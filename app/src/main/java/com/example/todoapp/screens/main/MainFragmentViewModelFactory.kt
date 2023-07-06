package com.example.todoapp.screens.main
import android.net.ConnectivityManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todoapp.di.app.AppScope
import com.example.todoapp.repository.TodoItemsRepository
import javax.inject.Inject

@AppScope
class MainFragmentViewModelFactory @Inject constructor(
    private val repository: TodoItemsRepository,
    private val connectivityManager: ConnectivityManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainFragmentViewModel(repository,connectivityManager) as T
    }
}