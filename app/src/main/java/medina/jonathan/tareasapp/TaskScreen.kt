package medina.jonathan.tareasapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun TaskScreen(
    viewModel: TaskViewModel = viewModel(
        factory = TaskViewModel.TaskVMFactory
    )
) {
    val tasks by viewModel.tasks.collectAsStateWithLifecycle()
    var newTaskText by remember { mutableStateOf("") }
    val searchInput by viewModel.searchInput.collectAsStateWithLifecycle()

    Scaffold { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(horizontal = 16.dp)) {
            Text(
                text = stringResource(R.string.app_title),
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SearchBar(
                    searchInput = searchInput,
                    onSearchInputChanged = { text -> viewModel.onSearchInputChanged(text)},
                    onSearchClicked = { viewModel.executeSearch() },
                    modifier = Modifier.weight(1f).padding(bottom = 6.dp)
                )
                OrderTasksDropDown(selectSort = { order -> viewModel.setOrder(order) })
            }

            Box(modifier = Modifier.weight(1f)) {
                if (tasks.isEmpty()) {
                    Text(
                        text = stringResource(R.string.msg_empty_tasks),
                        modifier = Modifier.fillMaxSize().padding(32.dp),
                        textAlign = TextAlign.Center
                    )
                } else {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(items = tasks, key = { task -> task.id }) { task ->
                            TaskItem(
                                task = task,
                                onToggleCompleted = { viewModel.toggleCompleted(task) },
                                onDelete = { viewModel.deleteTask(task) }
                            )
                        }
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = newTaskText,
                    onValueChange = { newTaskText = it },
                    placeholder = { Text(text = stringResource(R.string.new_task_title)) },
                    modifier = Modifier.weight(1f)
                )
                Button(
                    onClick = {
                        viewModel.addTask(newTaskText)
                        newTaskText = ""
                    }
                ) { Text(text = stringResource(R.string.add_button)) }
            }
        }
    }
}