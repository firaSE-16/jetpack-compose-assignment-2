// ui/detail/DetailViewModel.kt
package com.example.todo_list.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.todo_list.data.repository.TodoRepository
import com.example.todo_list.model.Todo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailViewModel(
    private val repository: TodoRepository,
    private val todoId: Int
) : ViewModel() {
    // Rest of your ViewModel implementation remains the same
    private val _uiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    init {
        loadTodo()
    }

    private fun loadTodo() {
        viewModelScope.launch {
            try {
                val todo = repository.getTodoById(todoId)
                if (todo != null) {
                    _uiState.value = DetailUiState.Success(todo)
                } else {
                    _uiState.value = DetailUiState.Error("Todo not found")
                }
            } catch (e: Exception) {
                _uiState.value = DetailUiState.Error("Failed to load todo")
            }
        }
    }

    class Factory(
        private val repository: TodoRepository,
        private val todoId: Int
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return DetailViewModel(repository, todoId) as T
        }
    }
}

sealed class DetailUiState {
    data object Loading : DetailUiState()
    data class Success(val todo: Todo) : DetailUiState()
    data class Error(val message: String) : DetailUiState()
}