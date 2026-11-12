package com.example.to_do_app.ui.todo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TaskList(
    tasks: List<Task>,
    onToggleTask: (Int) -> Unit,
    onRemoveTask: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(tasks, key = { it.id }) { task ->
            TaskCard(
                task = task,
                onToggle = { onToggleTask(task.id) },
                onRemove = { onRemoveTask(task.id) }
            )
        }
    }
}