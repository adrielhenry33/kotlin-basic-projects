package com.example.to_do_app.ui.todo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ConteudoTodo(
    typedText: String,
    taskList: List<Task>,
    onTypedTextChange: (String) -> Unit,
    onRegisterTask: () -> Unit,
    onToggleTask: (Int) -> Unit,
    onRemoveTask: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(top = 80.dp, start = 30.dp, end = 30.dp)
            .fillMaxSize(),
    ) {
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(
                12.dp
            )
        ) {

            TextField(
                valor = typedText,
                onTextChange = { text: String -> onTypedTextChange(text) },
                Modifier.weight(1f)
            )

            Button(
                onClick = { onRegisterTask() },
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier.height(OutlinedTextFieldDefaults.MinHeight),
            ) { Text("Adicionar") }


        }

        TaskList(
            onToggleTask = { value: Int ->  onToggleTask(value) },
            onRemoveTask = { value ->  onRemoveTask(value) },
            tasks = taskList
        )

    }
}