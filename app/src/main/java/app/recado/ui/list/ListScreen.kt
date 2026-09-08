package app.recado.ui.list

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.recado.R
import app.recado.data.Item
import app.recado.ui.ListUiState
import app.recado.ui.theme.Accent
import app.recado.ui.theme.DoneFill
import app.recado.ui.theme.FieldBg
import app.recado.ui.theme.Ink
import app.recado.ui.theme.Ink40
import app.recado.ui.theme.InkSecondary
import app.recado.ui.theme.Muted
import app.recado.ui.theme.Paper
import app.recado.ui.theme.RecadoText
import app.recado.ui.theme.RecadoTheme
import java.time.LocalDate
import java.util.Locale

private val RowHeight = 62.dp
private val CheckTarget = 48.dp
private val CheckRing = 22.dp

@Composable
fun ListScreen(
    state: ListUiState,
    onToggle: (Item) -> Unit,
    onLongPress: (Item) -> Unit,
    onUndo: () -> Unit,
    onClearDone: () -> Unit,
    onOpenAdd: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    val day = remember { dayLabel() }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding),
    ) {
        Column(Modifier.padding(start = 24.dp, end = 24.dp, top = 24.dp, bottom = 10.dp)) {
            Text(
                text = stringResource(R.string.list_title),
                style = RecadoText.screenTitle,
                color = Ink,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = stringResource(
                    R.string.list_meta,
                    day,
                    pluralStringResource(R.plurals.pending_count, state.pending, state.pending),
                ),
                style = RecadoText.meta,
                color = InkSecondary,
            )
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(bottom = 8.dp),
        ) {
            items(state.items, key = { it.id }) { item ->
                ItemRow(
                    item = item,
                    onToggle = { onToggle(item) },
                    onLongPress = { onLongPress(item) },
                )
            }
            // Va debajo de la lista, pegado al último tachado.
            if (state.allDone) {
                item(key = "all-done") {
                    AllDoneBlock(onUndo = onUndo, onClearDone = onClearDone)
                }
            }
        }

        AddFieldButton(onClick = onOpenAdd)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ItemRow(
    item: Item,
    onToggle: () -> Unit,
    onLongPress: () -> Unit,
) {
    val checkDescription = stringResource(
        if (item.done) R.string.cd_toggle_done else R.string.cd_toggle_pending,
    )
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(RowHeight)
            .combinedClickable(onClick = onToggle, onLongClick = onLongPress)
            .padding(start = 12.dp, end = 24.dp),
    ) {
        // Hit target de 48dp; el aro dibujado se queda en 22dp.
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(CheckTarget)
                .clip(CircleShape)
                .combinedClickable(onClick = onToggle, onLongClick = onLongPress)
                .semantics { contentDescription = checkDescription },
        ) {
            Check(done = item.done)
        }
        Spacer(Modifier.width(4.dp))
        Text(
            text = item.text,
            style = RecadoText.row,
            color = if (item.done) Muted else Ink,
            textDecoration = if (item.done) TextDecoration.LineThrough else null,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

/** Pendiente: aro ink. Hecho: disco gris. */
@Composable
private fun Check(done: Boolean) {
    Canvas(Modifier.size(CheckRing)) {
        val radius = size.minDimension / 2f
        val center = Offset(size.width / 2f, size.height / 2f)
        if (done) {
            drawCircle(color = DoneFill, radius = radius, center = center)
        } else {
            val stroke = 1.5.dp.toPx()
            drawCircle(
                color = Ink,
                radius = radius - stroke / 2f,
                center = center,
                style = Stroke(width = stroke),
            )
        }
    }
}

/** 0 pendientes y algo tachado. El único naranja acá es el punto. */
@Composable
private fun AllDoneBlock(
    onUndo: () -> Unit,
    onClearDone: () -> Unit,
) {
    Column(Modifier.padding(start = 24.dp, end = 24.dp, top = 14.dp, bottom = 8.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier
                    .size(7.dp)
                    .clip(CircleShape)
                    .background(Accent),
            )
            Spacer(Modifier.width(10.dp))
            Text(
                text = stringResource(R.string.all_done),
                style = RecadoText.row,
                color = Ink,
            )
        }
        Spacer(Modifier.height(2.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            TextAction(text = stringResource(R.string.undo), onClick = onUndo)
            TextAction(text = stringResource(R.string.clear_done), onClick = onClearDone)
        }
    }
}

@Composable
private fun TextAction(text: String, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        color = Paper,
        shape = RoundedCornerShape(8.dp),
    ) {
        Text(
            text = text,
            style = RecadoText.action,
            color = InkSecondary,
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 4.dp),
        )
    }
}

@Composable
private fun AddFieldButton(onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = FieldBg,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 8.dp, bottom = 16.dp)
            .height(56.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 8.dp, end = 16.dp),
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Accent),
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_add),
                    contentDescription = stringResource(R.string.cd_open_add),
                    tint = Paper,
                    modifier = Modifier.size(18.dp),
                )
            }
            Spacer(Modifier.width(12.dp))
            Text(
                text = stringResource(R.string.add_field),
                style = RecadoText.field,
                color = Ink40,
            )
        }
    }
}

/** "Lunes", "Martes"... en español y con mayúscula inicial. */
private fun dayLabel(): String {
    val locale = Locale.forLanguageTag("es")
    val day = LocalDate.now().dayOfWeek
        .getDisplayName(java.time.format.TextStyle.FULL, locale)
    return day.replaceFirstChar { it.titlecase(locale) }
}

@Preview(showBackground = true, backgroundColor = 0xFFF3EEE6)
@Composable
private fun ListScreenPreview() {
    RecadoTheme {
        ListScreen(
            state = ListUiState(
                items = listOf(
                    Item(id = 1, text = "Leche", order = 1),
                    Item(id = 2, text = "Huevos", order = 2),
                    Item(id = 3, text = "Pan", done = true, doneAt = 1L, order = 3),
                ),
                loading = false,
            ),
            onToggle = {},
            onLongPress = {},
            onUndo = {},
            onClearDone = {},
            onOpenAdd = {},
        )
    }
}
