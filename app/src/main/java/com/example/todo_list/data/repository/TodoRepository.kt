package com.example.todo_list.data.repository

import com.example.todo_list.data.local.TodoDao
import com.example.todo_list.data.local.TodoEntity
import com.example.todo_list.data.remote.TodoApi
import com.example.todo_list.model.Todo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.io.IOException

class TodoRepository(
    private val api: TodoApi,
    private val dao: TodoDao
) {
    fun getAllTodos(): Flow<List<Todo>> {
        return dao.getAllTodos().map { entities ->
            entities.map { it.toTodo() }
        }
    }

    suspend fun getTodoById(id: Int): Todo? {
        return dao.getTodoById(id)?.toTodo()
    }

    suspend fun refreshTodos() {
        try {
            val remoteTodos = api.getTodos()
            dao.insertTodos(remoteTodos.map { TodoEntity.fromTodo(it) })
        } catch (e: IOException) {
            // Handle network error
        } catch (e: HttpException) {
            // Handle API error
        }
    }
} 