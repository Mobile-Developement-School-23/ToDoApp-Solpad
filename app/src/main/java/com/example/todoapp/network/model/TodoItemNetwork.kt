package com.example.todoapp.network.model

import com.example.todoapp.model.Importance
import com.example.todoapp.model.TodoItem
import com.google.gson.annotations.SerializedName

data class TodoItemNetwork(
    @SerializedName("id")
    var id: String = "1",
    @SerializedName("text")
    val text: String = "content",
    @SerializedName("importance")
    val importance: String = "low",
    @SerializedName("deadline")
    val deadline: Long = 100000000,
    @SerializedName("done")
    val done: Boolean = false,
    @SerializedName("color")
    val color: String = "#FFFFFF",
    @SerializedName("created_at")
    val createdAt: Long = 100000000,
    @SerializedName("changed_at")
    val changedAt: Long = 100000000,
    @SerializedName("last_updated_by")
    val lastUpdatedBy: String = "kek"
) {
    fun mapToTodoItem() =
        TodoItem( id, text, Importance.valueOf(importance.uppercase()), done, createdAt, changedAt,deadline)

    companion object {
        fun TodoItem.mapToTodoItemNetwork(id: String) = TodoItemNetwork(
            id,
            text = content,
            importance = importance.name.lowercase(),
            deadline = deadline,
            done = flag,
            "#FFFFFF",
            createdAt = dateCreated,
            changedAt = dateChanged,
            "0"
        )
    }
}