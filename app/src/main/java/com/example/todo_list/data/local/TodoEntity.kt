package com.example.todo_list.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.todo_list.model.Todo

@Entity(tableName = "todos")
data class TodoEntity(
    @PrimaryKey
    val id: Int,
    val userId: Int,
    val title: String,
    val completed: Boolean
) {
    fun toTodo(): Todo = Todo(id, userId, title, completed)

    companion object {
        fun fromTodo(todo: Todo): TodoEntity =
            TodoEntity(todo.id, todo.userId, todo.title, todo.completed)
    }
}