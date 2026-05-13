package medina.jonathan.tareasapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import medina.jonathan.tareasapp.domain.TaskEntity
import medina.jonathan.tareasapp.presentation.TaskViewModel
import medina.jonathan.tareasapp.ui.theme.TareasAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            TareasAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun MainScreen(modifier: Modifier = Modifier) {

    val taskViewModel = TaskViewModel().eliminarTarea()

    var mostrarDialogoInsertar by remember { mutableStateOf(false) }
    var taskACompletar by remember { mutableStateOf<TaskEntity?>(null)}

    Column(
        modifier = modifier.fillMaxSize().padding(all = 32.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Mis Tareas", style = MaterialTheme.typography.titleLarge, fontSize = 36.sp)
        Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Button(
                onClick = { if (!mostrarDialogoInsertar) mostrarDialogoInsertar = true },
                modifier = modifier.background(color = MaterialTheme.colorScheme.onSecondary)
            ) {
                Text(
                    text = "Añadir tarea",
                    style = MaterialTheme.typography.bodyLarge,
                    fontSize = 22.sp,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.padding(10.dp)
                )
            }
        }

        if (tasks.isEmpty()) {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(16.dp)
            ) {
                items(tasks) { task ->
                    TaskCard(task, onLongClick = { taskACompletar = task })
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        } else {
            Text(
                text = "No hay tareas por el momento. Para añadir una tarea, haz clic en \"Añadir tarea\".",
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 22.sp,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }

        if (mostrarDialogoInsertar) {
            InsertTaskDialog(onConfirm = {  }) { }
        }
    }
}

@Composable
fun TaskCard(task: TaskEntity, modifier: Modifier = Modifier, onLongClick: () -> Unit ) {
    val completado = if (task.completado) { "✅" } else { "❌" }

    Card (
        modifier = modifier.fillMaxSize()
            .border(width = 1.dp, color = MaterialTheme.colorScheme.outline)
            .background(color = MaterialTheme.colorScheme.onPrimary, shape = RoundedCornerShape(12.dp))
            .combinedClickable(onClick = {}, onLongClick = onLongClick)
    ) {
        Text(text = task.titulo, style = MaterialTheme.typography.displayLarge)
        Text(text = "Completado: $completado", style = MaterialTheme.typography.displayLarge)
    }
}

@Composable
fun InsertTaskDialog(onDismiss: () -> Unit, onConfirm: (String) -> Unit) {
    val context = LocalContext.current
    var titulo by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nueva Tarea", style = MaterialTheme.typography.displayLarge) },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text("Título")
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = titulo,
                    onValueChange = {titulo = it},
                    label = { Text("Descripción")},
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (titulo.isNotBlank()) {
                        onConfirm(titulo)
                    } else {
                        Toast.makeText(context, "Ingresa una descripción válida para añadir la tarea.",
                            Toast.LENGTH_SHORT).show()
                    }
                }
            ) {
                Text(
                    text = "Añadir", style = MaterialTheme.typography.bodyLarge, fontSize = 22.sp,
                    color = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.padding(10.dp)
                )
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(
                    text = "Cancelar", style = MaterialTheme.typography.bodyLarge, fontSize = 22.sp,
                    color = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.padding(10.dp)
                )
            }
        }
    )
}