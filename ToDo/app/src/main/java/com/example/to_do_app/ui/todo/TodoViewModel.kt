package com.example.to_do_app.ui.todo

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

enum class TaskPriority { HIGH, MEDIUM, LOW }


data class Task(
    val id: Int,
    val priority: TaskPriority? = null,
    val title: String,
    val isTaskConcluded: Boolean = false
)

data class TodoState(
    val listTask: List<Task> = emptyList(),
    val typedText: String = ""
)

class TodoViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(TodoState())
    val uiState: StateFlow<TodoState> = _uiState.asStateFlow()

    private var nextId = 0

    fun onTypedTextChange(text: String) {
        _uiState.update { it.copy(typedText = text) }
    }

    fun registerTask() {
        if (_uiState.value.typedText.trim().isEmpty()) return
        val title = _uiState.value.typedText.trim()
        _uiState.update {
            it.copy(
                listTask = it.listTask + Task(id = nextId++, title = title),
                typedText = ""
            )
        }
    }

    fun removeTask(id: Int) {
        _uiState.update { it.copy(listTask = it.listTask.filter { task -> task.id != id }) }
    }

    fun taskConcluded(id: Int) {
        _uiState.update {
            it.copy(listTask = it.listTask.map { task ->
                if (task.id == id) task.copy(isTaskConcluded = !task.isTaskConcluded) else task
            })
        }
    }

}