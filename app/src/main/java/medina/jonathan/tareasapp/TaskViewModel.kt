package medina.jonathan.tareasapp

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TaskViewModel(private val taskDao: TaskDao): ViewModel() {

    val tasks: StateFlow<List<TaskEntity>> = taskDao.getAllTasks()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun addTask(titulo: String) {
        if (titulo.isBlank()) return
        viewModelScope.launch {
            taskDao.insert(TaskEntity(titulo = titulo))
        }
    }

    fun toggleCompleted(task: TaskEntity) {
        viewModelScope.launch { taskDao.update(task.copy(completado = !task.completado)) }
    }

    fun deleteTask(task: TaskEntity) {
        viewModelScope.launch { taskDao.delete(task) }
    }

    // Factory
    companion object {
        val TaskVMFactory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as Application
                val taskDao = AppDatabase.getInstance(app).taskDao()
                TaskViewModel(taskDao)
            }
        }
    }
}