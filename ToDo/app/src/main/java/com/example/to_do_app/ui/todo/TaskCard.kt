package com.example.to_do_app.ui.todo

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration


@Composable
fun TaskCard(
    task: Task,
    onToggle: () -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier,
) {

    var showDialog by rememberSaveable { mutableStateOf(false) }

    Card(modifier = modifier.fillMaxWidth(), shape = RoundedCornerShape(4.dp)) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
        ) {
            Checkbox(
                checked = task.isTaskConcluded,
                onCheckedChange = { onToggle() },
            )

            Text(
                text = task.title,
                Modifier.weight(1f),
                style = TextStyle(textDecoration = if (task.isTaskConcluded) TextDecoration.LineThrough else null)
            )

            Button(
                onClick = { showDialog = true },
                shape = RoundedCornerShape(4.dp)
            ) { Text("Remover") }

            if (showDialog) {
                DialogComponent(
                    onConfirm = {
                        onRemove()
                        showDialog = false
                    },
                    onCancel = { showDialog = false },
                    title = task.title,
                    modifier = Modifier
                )
            }

        }

    }

}


@Preview(showBackground = true)
@Composable
private fun TaskCardPreview() {
    TaskCard(
        task = Task(id = 0, title = "Comprar pão"),
        onToggle = {},
        onRemove = {},
    )
}