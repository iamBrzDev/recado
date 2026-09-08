package app.recado.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Un renglón del mandado.
 *
 * `order` se guarda como columna `sort_order`: "order" es palabra reservada en SQL
 * y no vale la pena pelearse con el quoting en cada query.
 */
@Entity(tableName = "items")
data class Item(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val text: String,
    val note: String? = null,
    val done: Boolean = false,
    val doneAt: Long? = null,
    @ColumnInfo(name = "sort_order") val order: Int = 0,
)
