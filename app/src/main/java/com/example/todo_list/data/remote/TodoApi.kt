package com.example.todo_list.data.remote

import com.example.todo_list.model.Todo
import retrofit2.http.GET
import retrofit2.http.Path

interface TodoApi {
    @GET("todos")
    suspend fun getTodos(): List<Todo>

    @GET("todos/{id}")
    suspend fun getTodoById(@Path("id") id: Int): Todo
}