package medina.jonathan.tareasapp

import android.app.Application
import androidx.compose.ui.text.style.TextDecoration.Companion.combine
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class TasksOrder { RECENT, OLDEST, A_Z, Z_A }

class TaskViewModel(private val taskDao: TaskDao): ViewModel() {

    private val _searchInput = MutableStateFlow("")
    val searchInput: StateFlow<String> = _searchInput.asStateFlow()

    private val _activeQuery =  MutableStateFlow("")
    private val _order =  MutableStateFlow(TasksOrder.RECENT)

    @OptIn(ExperimentalCoroutinesApi::class)
    val tasks: StateFlow<List<TaskEntity>> = combine(_activeQuery, _order) { q, o -> q to o }
        .flatMapLatest { (query, order) -> taskDao.searchTasks(query, order) }
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

    fun onSearchInputChanged(text: String) {
        _searchInput.value = text
    }

    fun executeSearch() {
        _activeQuery.value = _searchInput.value.trim()
    }

    fun setOrder(order: TasksOrder) {
        _order.value = order
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