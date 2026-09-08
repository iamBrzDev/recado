package app.recado.data

import kotlinx.coroutines.flow.Flow

/**
 * @param onChanged se dispara después de cada escritura. Es lo que refresca el widget;
 * el repo no sabe nada de widgets.
 */
class ItemRepository(
    private val dao: ItemDao,
    private val onChanged: suspend () -> Unit = {},
) {

    val items: Flow<List<Item>> = dao.observeAll()

    suspend fun add(text: String) {
        val clean = text.trim()
        if (clean.isEmpty()) return
        dao.insert(Item(text = clean, order = dao.maxOrder() + 1))
        onChanged()
    }

    suspend fun toggle(item: Item) {
        val done = !item.done
        dao.setDone(item.id, done, if (done) System.currentTimeMillis() else null)
        onChanged()
    }

    suspend fun delete(item: Item) {
        dao.delete(item.id)
        onChanged()
    }

    suspend fun clearDone() {
        dao.deleteDone()
        onChanged()
    }

    /** Destacha el último ítem tachado. Si no hay ninguno, no hace nada. */
    suspend fun undoLastDone() {
        val id = dao.lastDoneId() ?: return
        dao.setDone(id, done = false, doneAt = null)
        onChanged()
    }
}
