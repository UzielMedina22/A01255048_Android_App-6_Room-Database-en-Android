package medina.jonathan.tareasapp

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Query(value = "SELECT * FROM tasks")
    fun getAllTasks(): Flow<List<TaskEntity>>

    @Query(value = """
        SELECT * FROM tasks
        WHERE titulo LIKE '%' || :query || '%'
        ORDER BY 
            CASE WHEN :order = 'RECENT' THEN creado_en END DESC,
            CASE WHEN :order = 'OLDEST' THEN creado_en END ASC,
            CASE WHEN :order = 'A_Z' THEN titulo END ASC,
            CASE WHEN :order = 'Z_A' THEN titulo END DESC
    """)
    fun searchTasks(query: String, order: TasksOrder): Flow<List<TaskEntity>>

    @Insert
    suspend fun insert(task: TaskEntity)

    @Update
    suspend fun update(task: TaskEntity)

    @Delete
    suspend fun delete(task: TaskEntity)
}