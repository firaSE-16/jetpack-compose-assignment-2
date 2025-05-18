package com.example.todo_list

import android.app.Application
import android.content.Context
import com.example.todo_list.data.local.TodoDatabase
import com.example.todo_list.data.remote.TodoApi
import com.example.todo_list.data.repository.TodoRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TodoApp : Application() {
    lateinit var repository: TodoRepository
        private set

    override fun onCreate() {
        super.onCreate()
        initialize()
    }

    private fun initialize() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(TodoApi::class.java)
        val database = TodoDatabase.getDatabase(this)
        repository = TodoRepository(api, database.todoDao())
    }

    companion object {
        fun getRepository(context: Context): TodoRepository {
            return (context.applicationContext as TodoApp).repository
        }
    }
} 