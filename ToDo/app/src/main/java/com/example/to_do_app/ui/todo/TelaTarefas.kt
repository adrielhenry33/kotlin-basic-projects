package com.example.to_do_app.ui.todo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun TelaTarefas(modifier: Modifier = Modifier, todoViewModel: TodoViewModel = viewModel()) {

    val state by todoViewModel.uiState.collectAsStateWithLifecycle()


    ConteudoTodo(
        onTypedTextChange = todoViewModel::onTypedTextChange,
        onRegisterTask = todoViewModel::registerTask,
        onRemoveTask = todoViewModel::removeTask,
        onToggleTask = todoViewModel::taskConcluded,
        taskList = state.listTask,
        typedText = state.typedText,
        modifier = modifier

    )
}


