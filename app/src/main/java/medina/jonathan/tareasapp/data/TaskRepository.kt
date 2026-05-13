package medina.jonathan.tareasapp.data

import androidx.annotation.WorkerThread
import medina.jonathan.tareasapp.domain.TaskEntity

class TaskRepository(private val taskDao: TaskDao) {
    val tasks = taskDao.getAllTasks()

    @WorkerThread
    suspend fun insertTask(task: TaskEntity) {
        taskDao.insert(task)
    }

    @WorkerThread
    suspend fun updateTask(task: TaskEntity) {
        taskDao.update(task)
    }

    @WorkerThread
    suspend fun deleteTask(task: TaskEntity) {
        taskDao.delete(task)
    }
}