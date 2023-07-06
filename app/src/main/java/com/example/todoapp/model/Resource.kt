package com.example.todoapp.model

sealed class Resource(
    val message: String? = null
) {
    class Error(message: String): Resource(message)
    class Success: Resource()
}