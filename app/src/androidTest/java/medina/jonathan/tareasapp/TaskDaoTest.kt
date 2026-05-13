package medina.jonathan.tareasapp

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import medina.jonathan.tareasapp.data.AppDatabase
import medina.jonathan.tareasapp.data.TaskDao
import medina.jonathan.tareasapp.domain.TaskEntity
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TaskDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var dao: TaskDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries().build()

        dao = db.taskDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun insertarTarea() = runTest {
        dao.insert(TaskEntity(titulo = "Comprar jugo, pan, huevos y leche."))
        val tasks = dao.getAllTasks().first()

        assertEquals(1, tasks.size)
        assertEquals("Comprar jugo, pan, huevos y leche.", tasks[0].titulo)
    }

    @Test
    fun actualizarTarea() = runTest {
        dao.insert(TaskEntity(titulo = "Implementar operaciones CRUD de los productos."))

        val tasksOriginales = dao.getAllTasks().first()
        assertEquals(false, tasksOriginales[0].completado)
        dao.update(tasksOriginales[0].copy(completado = true))

        val taskActualizada = dao.getAllTasks().first().first()
        assertTrue(taskActualizada.completado)
    }

    @Test
    fun eliminarTarea() = runTest {
        dao.insert(TaskEntity(titulo = "Prueba"))

        val task = dao.getAllTasks().first().first()
        dao.delete(task)

        val tasks = dao.getAllTasks().first()
        assertTrue(tasks.isEmpty())
    }
}