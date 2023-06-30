package com.example.todoapp.network.model

import com.google.gson.annotations.SerializedName

data class SetItemsRequest(
    @SerializedName("status")
    val status: String = "ok",
    @SerializedName("list")
    val todoItemsNetwork: List<TodoItemNetwork>
)