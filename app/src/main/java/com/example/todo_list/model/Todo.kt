package com.example.todo_list.model

data class Todo(
    val id: Int,
    val userId: Int,
    val title: String,
    val completed: Boolean
)