package app.recado.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

/** recado es papel claro siempre: no seguimos el modo del sistema en v1. */
private val RecadoColorScheme = lightColorScheme(
    primary = Accent,
    onPrimary = Paper,
    secondary = InkSecondary,
    onSecondary = Paper,
    background = Paper,
    onBackground = Ink,
    surface = Paper,
    onSurface = Ink,
    surfaceVariant = FieldBg,
    onSurfaceVariant = InkSecondary,
    outline = Ink40,
    outlineVariant = Ink40,
    error = Accent,
    onError = Paper,
    scrim = Ink,
)

@Composable
fun RecadoTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = RecadoColorScheme,
        typography = RecadoTypography,
        content = content,
    )
}
