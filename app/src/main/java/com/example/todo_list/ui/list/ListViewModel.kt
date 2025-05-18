package com.example.todo_list.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.todo_list.data.repository.TodoRepository
import com.example.todo_list.model.Todo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ListViewModel(
    private val repository: TodoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ListUiState>(ListUiState.Loading)
    val uiState: StateFlow<ListUiState> = _uiState.asStateFlow()

    init {
        loadTodos()
    }

    private fun loadTodos() {
        viewModelScope.launch {
            try {
                repository.refreshTodos()
                repository.getAllTodos().collect { todos ->
                    _uiState.value = ListUiState.Success(todos)
                }
            } catch (e: Exception) {
                _uiState.value = ListUiState.Error("Failed to load todos")
            }
        }
    }

    fun refresh() {
        loadTodos()
    }

    class Factory(private val repository: TodoRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ListViewModel(repository) as T
        }
    }
}

sealed class ListUiState {
    data object Loading : ListUiState()
    data class Success(val todos: List<Todo>) : ListUiState()
    data class Error(val message: String) : ListUiState()
}