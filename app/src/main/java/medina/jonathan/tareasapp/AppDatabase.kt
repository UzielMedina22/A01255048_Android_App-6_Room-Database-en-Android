package medina.jonathan.tareasapp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.concurrent.Volatile

@Database(entities = [TaskEntity::class], version = 1)
abstract class AppDatabase: RoomDatabase() {

    abstract fun taskDao(): TaskDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        private val INITIAL_TASKS = listOf<TaskEntity>(
            TaskEntity(titulo = "Publicar visualizaciones al feed.", completado = true),
            TaskEntity(titulo = "Cargar visualizaciones en el feed.", completado = true),
            TaskEntity(titulo = "Modificar permisos en las visualizaciones.", completado = true),
            TaskEntity(titulo = "Crear el componente de las gráficas.", completado = true),
            TaskEntity(titulo = "Remover el acceso compartido a una visualización.", completado = true),
            TaskEntity(titulo = "Habilitar interacción con los datos.", completado = true),
            TaskEntity(titulo = "Añadir zoom interactivo en pantalla completa.", completado = false),
            TaskEntity(titulo = "Sincronizar el diseño de UI con iOS.", completado = true),
            TaskEntity(titulo = "Mejorar el diseño de la interfaz al rotar.", completado = false)
        )

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "tasks_db"
                ).addCallback(object: RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        CoroutineScope(Dispatchers.IO).launch {
                            val dao = getInstance(context).taskDao()
                            INITIAL_TASKS.forEach { task ->
                                dao.insert(task)
                            }
                        }
                    }
                })
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}