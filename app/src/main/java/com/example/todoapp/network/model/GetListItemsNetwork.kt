package com.example.todoapp.network.model

import com.google.gson.annotations.SerializedName


data class GetListItemsNetwork(
    @SerializedName("status")
        val status: String,
    @SerializedName("list")
        val listItemsNetwork: List<TodoItemNetwork>,
    @SerializedName("revision")
        val revision: Int
    )