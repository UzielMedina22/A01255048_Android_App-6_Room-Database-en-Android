package medina.jonathan.tareasapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val titulo: String,
    val completado: Boolean = false,
    val creado_en: Long = System.currentTimeMillis()
)