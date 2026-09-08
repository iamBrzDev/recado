package app.recado.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {

    /** Pendientes arriba en el orden en que se apuntaron, tachados al final. */
    @Query("SELECT * FROM items ORDER BY done ASC, sort_order ASC, id ASC")
    fun observeAll(): Flow<List<Item>>

    @Query("SELECT COALESCE(MAX(sort_order), 0) FROM items")
    suspend fun maxOrder(): Int

    @Insert
    suspend fun insert(item: Item): Long

    @Query("UPDATE items SET done = :done, doneAt = :doneAt WHERE id = :id")
    suspend fun setDone(id: Long, done: Boolean, doneAt: Long?)

    @Query("DELETE FROM items WHERE id = :id")
    suspend fun delete(id: Long)

    @Query("DELETE FROM items WHERE done = 1")
    suspend fun deleteDone()

    /** El último que se tachó: es lo que restaura "Deshacer". */
    @Query("SELECT id FROM items WHERE done = 1 ORDER BY doneAt DESC, id DESC LIMIT 1")
    suspend fun lastDoneId(): Long?
}
