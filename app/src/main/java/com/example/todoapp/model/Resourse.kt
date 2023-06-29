package com.example.todoapp.model

sealed class Resourse(
    val message: String? = null
) {
    class Error(message: String): Resourse(message)
    class Success: Resourse()
}