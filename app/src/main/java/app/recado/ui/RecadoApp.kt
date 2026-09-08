package app.recado.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.recado.R
import app.recado.data.Item
import app.recado.ui.add.AddSheet
import app.recado.ui.empty.EmptyScreen
import app.recado.ui.list.ListScreen
import app.recado.ui.theme.Ink
import app.recado.ui.theme.InkSecondary
import app.recado.ui.theme.Paper
import app.recado.ui.theme.RecadoText

@Composable
fun RecadoApp(
    viewModel: ItemsViewModel,
    openAddRequest: Boolean = false,
    onOpenAddConsumed: () -> Unit = {},
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var showAdd by rememberSaveable { mutableStateOf(false) }
    var pendingDelete by remember { mutableStateOf<Item?>(null) }

    // Sirve tanto para arranque en frío desde el widget como para onNewIntent.
    LaunchedEffect(openAddRequest) {
        if (openAddRequest) {
            showAdd = true
            onOpenAddConsumed()
        }
    }

    Scaffold(
        containerColor = Paper,
        contentWindowInsets = WindowInsets.safeDrawing,
    ) { padding ->
        when {
            // Papel en blanco mientras Room contesta: si no, parpadea el empty.
            state.loading -> Box(Modifier.fillMaxSize())

            state.isEmpty -> EmptyScreen(
                onOpenAdd = { showAdd = true },
                contentPadding = padding,
            )

            else -> ListScreen(
                state = state,
                onToggle = viewModel::toggle,
                onLongPress = { pendingDelete = it },
                onUndo = viewModel::undo,
                onClearDone = viewModel::clearDone,
                onOpenAdd = { showAdd = true },
                contentPadding = padding,
            )
        }
    }

    if (showAdd) {
        AddSheet(
            onDismiss = { showAdd = false },
            // Guardar no cierra: el sheet se va con Cancelar o arrastrando.
            onSave = viewModel::add,
        )
    }

    pendingDelete?.let { item ->
        DeleteDialog(
            item = item,
            onConfirm = {
                viewModel.delete(item)
                pendingDelete = null
            },
            onDismiss = { pendingDelete = null },
        )
    }
}

@Composable
private fun DeleteDialog(
    item: Item,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Paper,
        title = {
            Text(
                text = stringResource(R.string.delete_title),
                style = RecadoText.headline.copy(fontSize = 20.sp, lineHeight = 26.sp),
                color = Ink,
            )
        },
        text = {
            Text(
                text = item.text,
                style = RecadoText.field,
                color = InkSecondary,
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    text = stringResource(R.string.delete_confirm),
                    style = RecadoText.action,
                    color = Ink,
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = stringResource(R.string.cancel),
                    style = RecadoText.action,
                    color = InkSecondary,
                )
            }
        },
    )
}
