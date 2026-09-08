package app.recado.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import app.recado.RecadoGraph
import app.recado.data.Item
import app.recado.data.ItemRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ListUiState(
    val items: List<Item> = emptyList(),
    /** true hasta la primera emisión de Room: evita que parpadee el empty. */
    val loading: Boolean = true,
) {
    val pending: Int get() = items.count { !it.done }
    val hasDone: Boolean get() = items.any { it.done }
    val isEmpty: Boolean get() = items.isEmpty()

    /** Nada pendiente pero algo tachado: "Listo por hoy." */
    val allDone: Boolean get() = hasDone && pending == 0
}

class ItemsViewModel(private val repo: ItemRepository) : ViewModel() {

    val state: StateFlow<ListUiState> = repo.items
        .map { ListUiState(items = it, loading = false) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ListUiState(),
        )

    fun add(text: String) {
        viewModelScope.launch { repo.add(text) }
    }

    fun toggle(item: Item) {
        viewModelScope.launch { repo.toggle(item) }
    }

    fun delete(item: Item) {
        viewModelScope.launch { repo.delete(item) }
    }

    fun undo() {
        viewModelScope.launch { repo.undoLastDone() }
    }

    fun clearDone() {
        viewModelScope.launch { repo.clearDone() }
    }

    companion object {
        fun factory(context: Context): ViewModelProvider.Factory {
            val appContext = context.applicationContext
            return viewModelFactory {
                initializer { ItemsViewModel(RecadoGraph.repository(appContext)) }
            }
        }
    }
}
