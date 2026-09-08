package app.recado.ui.empty

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.recado.R
import app.recado.ui.theme.Ink
import app.recado.ui.theme.Ink40
import app.recado.ui.theme.InkSecondary
import app.recado.ui.theme.Muted
import app.recado.ui.theme.RecadoText
import app.recado.ui.theme.RecadoTheme

/**
 * Primera vez: nada apuntado todavía.
 * Sin naranja: acá el campo es una puerta, no el CTA.
 */
@Composable
fun EmptyScreen(
    onOpenAdd: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding)
            .padding(horizontal = 24.dp),
    ) {
        Text(
            text = stringResource(R.string.wordmark),
            style = RecadoText.wordmark,
            color = Ink40,
            modifier = Modifier.padding(top = 20.dp),
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = stringResource(R.string.brand_mark),
                style = RecadoText.brandMark,
                color = Ink,
            )
            Spacer(Modifier.height(20.dp))
            Text(
                text = stringResource(R.string.empty_headline),
                style = RecadoText.screenTitle,
                color = Ink,
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.empty_copy),
                style = RecadoText.field,
                color = Muted,
            )
            Spacer(Modifier.height(28.dp))
            OutlinedAddField(onClick = onOpenAdd)
        }

        Text(
            text = stringResource(R.string.empty_footer),
            style = RecadoText.meta,
            color = InkSecondary,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 28.dp),
        )
    }
}

@Composable
private fun OutlinedAddField(onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        color = Color.Transparent,
        border = BorderStroke(1.dp, Ink40),
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
    ) {
        Box(
            contentAlignment = Alignment.CenterStart,
            modifier = Modifier.padding(horizontal = 18.dp),
        ) {
            Text(
                text = stringResource(R.string.empty_field),
                style = RecadoText.field,
                color = Ink40,
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF3EEE6)
@Composable
private fun EmptyScreenPreview() {
    RecadoTheme { EmptyScreen(onOpenAdd = {}) }
}
