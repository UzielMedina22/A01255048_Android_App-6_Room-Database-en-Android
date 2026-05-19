package medina.jonathan.tareasapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TaskItem(task: TaskEntity, onToggleCompleted: () -> Unit, onDelete: () -> Unit) {
    val formattedDate = remember { SimpleDateFormat("dd/MM HH:mm", Locale("es")) }
    val textDate = remember(task.creado_en) { formattedDate.format(Date(task.creado_en)) }

    var showDeleteDialog by remember { mutableStateOf(false) }

    val swipeToDismissBoxState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            if (it == SwipeToDismissBoxValue.EndToStart) showDeleteDialog = true
            it == SwipeToDismissBoxValue.Settled
        }
    )

    SwipeToDismissBox(
        state = swipeToDismissBoxState,
        modifier = Modifier.fillMaxSize(),
        backgroundContent = {
            when (swipeToDismissBoxState.dismissDirection) {
                SwipeToDismissBoxValue.EndToStart -> {
                    Icon(
                        painter = painterResource(R.drawable.outline_delete_32),
                        contentDescription = stringResource(R.string.delete_task_msg),
                        modifier = Modifier.fillMaxSize()
                            .background(color = Color.Red.copy(alpha = 0.35f))
                            .wrapContentSize(Alignment.CenterEnd)
                    )
                }
                else -> {}
            }
        }
    ) {
        ListItem(
            leadingContent = {
                Checkbox(
                    checked = task.completado,
                    onCheckedChange = { onToggleCompleted() }
                )
            },
            headlineContent = {
                Text(
                    text = task.titulo,
                    textDecoration = if (task.completado) TextDecoration.LineThrough else null,
                    color = if (task.completado) {
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    }
                )
            },
            supportingContent = { Text(text = textDate) },
            trailingContent = {
                Icon(
                    painter = painterResource(R.drawable.outline_swipe_left_alt_32),
                    contentDescription = stringResource(R.string.delete_task_msg),
                )
            }
        )
    }

    if (showDeleteDialog) {
        DeleteTaskDialog(
            onConfirm = {
                onDelete()
                showDeleteDialog = false
            },
            onDismiss = {
                showDeleteDialog = false
            }
        )
    }
}

@Composable
fun DeleteTaskDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        title = { Text(text = stringResource(R.string.delete_task_msg)) },
        text = { Text(text = stringResource(R.string.delete_task_dialog_question)) },
        onDismissRequest = { onDismiss() },
        confirmButton = {
            Button(onClick = { onConfirm() }) {
                Text(text = stringResource(R.string.delete_task_confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text(text = stringResource(R.string.dismiss))
            }
        }
    )
}