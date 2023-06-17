package com.example.todoapp.util

import androidx.recyclerview.widget.DiffUtil
import com.example.todoapp.model.TodoItem


class ItemDiffCallback : DiffUtil.ItemCallback<TodoItem>() {
    override fun areItemsTheSame(oldItem: TodoItem, newItem: TodoItem) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: TodoItem, newItem: TodoItem) =
        oldItem == newItem
}