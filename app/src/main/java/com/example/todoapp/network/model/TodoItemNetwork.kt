package com.example.todoapp.network.model

import com.example.todoapp.model.Importance
import com.example.todoapp.model.TodoItem
import com.google.gson.annotations.SerializedName

data class TodoItemNetwork(
    @SerializedName("id")
    var id: String = "13",
    @SerializedName("text")
    val text: String = "text",
    @SerializedName("importance")
    val importance: String = "low",
    @SerializedName("deadline")
    val deadline: Long = 100000,
    @SerializedName("done")
    val done: Boolean = false,
    @SerializedName("color")
    val color: String = "#FFFFFF",
    @SerializedName("created_at")
    val createdAt: Long = 100000,
    @SerializedName("changed_at")
    val changedAt: Long = 100000,
    @SerializedName("last_updated_by")
    val lastUpdatedBy: String = "model"
) {
    fun mapToTodoItem() = TodoItem(
        id, text, Importance.valueOf(importance.uppercase()), done, deadline, createdAt, changedAt
    )

    companion object {
        fun TodoItem.mapToTodoItemNetwork(id: String) = TodoItemNetwork(
            id,
            content,
            importance.name.lowercase(),
            deadline,
            flag,
            "#FFFFFF",
            dateCreated,
            dateChanged,
            "0"
        )
    }
}