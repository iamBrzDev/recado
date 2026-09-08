package app.recado.ui.add

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import app.recado.R
import app.recado.ui.theme.Accent
import app.recado.ui.theme.DoneFill
import app.recado.ui.theme.Ink
import app.recado.ui.theme.Ink40
import app.recado.ui.theme.InkSecondary
import app.recado.ui.theme.Paper
import app.recado.ui.theme.RecadoText

/**
 * Apuntar un recado. Sheet, no pantalla: el mandado se queda atrás a la vista.
 * No se cierra al guardar; se limpia el campo para seguir apuntando.
 * El único naranja es el botón de guardar.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSheet(
    onDismiss: () -> Unit,
    onSave: (String) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Paper,
        dragHandle = null,
        shape = RoundedCornerShape(topStart = 22.dp, topEnd = 22.dp),
    ) {
        AddSheetContent(onCancel = onDismiss, onSave = onSave)
    }
}

@Composable
private fun AddSheetContent(
    onCancel: () -> Unit,
    onSave: (String) -> Unit,
) {
    var text by rememberSaveable { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    val canSave = text.isNotBlank()

    // Guardar deja el sheet abierto y el campo vacío: se apuntan varias cosas seguidas.
    fun save() {
        if (!canSave) return
        onSave(text.trim())
        text = ""
    }

    LaunchedEffect(Unit) { focusRequester.requestFocus() }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .imePadding()
            .padding(horizontal = 20.dp)
            .padding(top = 14.dp, bottom = 20.dp),
    ) {
        Surface(
            onClick = onCancel,
            color = Paper,
            shape = RoundedCornerShape(8.dp),
        ) {
            Text(
                text = stringResource(R.string.cancel),
                style = RecadoText.action,
                color = InkSecondary,
                modifier = Modifier.padding(vertical = 10.dp, horizontal = 4.dp),
            )
        }

        Spacer(Modifier.height(14.dp))
        Text(
            text = stringResource(R.string.add_headline),
            style = RecadoText.headline,
            color = Ink,
            modifier = Modifier.padding(horizontal = 4.dp),
        )
        Spacer(Modifier.height(18.dp))

        Row(verticalAlignment = Alignment.Bottom) {
            BasicTextField(
                value = text,
                onValueChange = { text = it },
                textStyle = RecadoText.input.copy(color = Ink),
                cursorBrush = SolidColor(Accent),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Done,
                ),
                keyboardActions = KeyboardActions(onDone = { save() }),
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 4.dp, vertical = 8.dp)
                    .focusRequester(focusRequester),
                decorationBox = { inner ->
                    if (text.isEmpty()) {
                        Text(
                            text = stringResource(R.string.add_placeholder),
                            style = RecadoText.input,
                            color = Ink40,
                        )
                    }
                    inner()
                },
            )
            Spacer(Modifier.width(12.dp))
            SaveButton(enabled = canSave, onClick = { save() })
        }

        Spacer(Modifier.height(18.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            // Los chips apuntan el ítem de una y dejan el campo limpio.
            listOf(R.string.chip_milk, R.string.chip_eggs, R.string.chip_bread).forEach { res ->
                val label = stringResource(res)
                Chip(
                    label = label,
                    onClick = {
                        onSave(label)
                        text = ""
                    },
                )
            }
        }
    }
}

@Composable
private fun SaveButton(enabled: Boolean, onClick: () -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(52.dp),
    ) {
        Surface(
            onClick = onClick,
            enabled = enabled,
            shape = CircleShape,
            color = if (enabled) Accent else DoneFill,
            modifier = Modifier.size(52.dp),
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_up),
                    contentDescription = stringResource(R.string.add_save),
                    tint = Paper,
                    modifier = Modifier.size(22.dp),
                )
            }
        }
    }
}

@Composable
private fun Chip(label: String, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        color = Color.Transparent,
        border = BorderStroke(1.dp, Ink40),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .height(38.dp)
                .background(Color.Transparent)
                .padding(horizontal = 16.dp),
        ) {
            Text(
                text = label,
                style = RecadoText.action,
                color = InkSecondary,
            )
        }
    }
}
