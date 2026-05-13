package medina.jonathan.tareasapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import medina.jonathan.tareasapp.data.AppDatabase
import medina.jonathan.tareasapp.domain.TaskEntity

class TaskViewModel(private val taskRepository: AppDatabase): ViewModel() {
    val tasks = taskRepository.taskDao().getAllTasks().stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(), emptyList()
    )

    fun insertarTarea(titulo: String) {
        val task = TaskEntity(titulo = titulo)
        viewModelScope.launch { taskRepository.taskDao().insert(task) }
    }

    fun actualizarTarea(task: TaskEntity) {
        viewModelScope.launch { taskRepository.taskDao().update(task) }
    }

    fun eliminarTarea(task: TaskEntity) {
        viewModelScope.launch { taskRepository.taskDao().update(task) }
    }
}