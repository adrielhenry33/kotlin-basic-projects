package com.example.to_do_app.ui.todo

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview(showBackground = true)
@Composable
private fun DialogPreview() {
    DialogComponent(
        onConfirm = {},
        onCancel = {},
        "Teste",
        modifier = Modifier
    )
}


@Composable
fun DialogComponent(
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    title: String,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onCancel,
        dismissButton = { TextButton(onClick = onCancel) { Text("Cancelar") } },
        confirmButton = { TextButton(onClick = onConfirm) { Text("Confirmar") } },
        modifier = modifier,
        title = { Text("Deletar atividade", fontWeight = FontWeight.Bold) },
        text = { Text(text = "Deseja remover a tarefa? $title") },
        shape = RoundedCornerShape(8.dp),
    )


}