package app.recado.widget

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextDecoration
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import app.recado.MainActivity
import app.recado.R
import app.recado.data.Item
import app.recado.data.RecadoDatabase
import app.recado.ui.theme.Accent
import app.recado.ui.theme.Ink
import app.recado.ui.theme.Ink40
import app.recado.ui.theme.InkSecondary
import app.recado.ui.theme.Muted
import app.recado.ui.theme.Paper

/** Cuántas filas entran en 4x2. */
private const val MAX_ROWS = 4

/**
 * Widget 4x2: el mandado de un vistazo.
 * Los checks son sólo dibujo — tachar se hace en la app.
 */
class RecadoWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val dao = RecadoDatabase.get(context).itemDao()
        provideContent {
            val items by dao.observeAll().collectAsState(initial = emptyList())
            WidgetBody(items)
        }
    }
}

@Composable
private fun WidgetBody(items: List<Item>) {
    val context = LocalContext.current
    // observeAll() ya viene con pendientes primero y tachados al final.
    val visible = items.take(MAX_ROWS)
    val rest = items.size - visible.size

    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(Paper)
            .cornerRadius(16.dp)
            .clickable(actionStartActivity(openAppIntent(context)))
            .padding(horizontal = 14.dp, vertical = 10.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = GlanceModifier.fillMaxWidth(),
        ) {
            Text(
                text = context.getString(R.string.list_title),
                style = TextStyle(
                    color = ColorProvider(Ink),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                ),
            )
            Spacer(GlanceModifier.defaultWeight())
            Text(
                text = context.getString(R.string.wordmark),
                style = TextStyle(color = ColorProvider(Ink40), fontSize = 11.sp),
            )
        }

        Spacer(GlanceModifier.height(6.dp))

        if (visible.isEmpty()) {
            Text(
                text = context.getString(R.string.empty_copy),
                style = TextStyle(color = ColorProvider(Muted), fontSize = 13.sp),
            )
        } else {
            visible.forEach { item -> WidgetRow(item) }
        }

        Spacer(GlanceModifier.defaultWeight())

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = GlanceModifier.fillMaxWidth(),
        ) {
            if (rest > 0) {
                Text(
                    text = context.getString(R.string.widget_more, rest),
                    style = TextStyle(color = ColorProvider(InkSecondary), fontSize = 12.sp),
                )
            }
            Spacer(GlanceModifier.defaultWeight())
            Text(
                text = context.getString(R.string.empty_field),
                style = TextStyle(
                    color = ColorProvider(Accent),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                ),
                modifier = GlanceModifier.clickable(actionStartActivity(addIntent(context))),
            )
        }
    }
}

@Composable
private fun WidgetRow(item: Item) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = GlanceModifier.fillMaxWidth().padding(vertical = 3.dp),
    ) {
        Image(
            provider = ImageProvider(
                if (item.done) R.drawable.ic_widget_done else R.drawable.ic_widget_ring,
            ),
            contentDescription = null,
            modifier = GlanceModifier.size(14.dp),
        )
        Spacer(GlanceModifier.width(8.dp))
        Text(
            text = item.text,
            maxLines = 1,
            style = TextStyle(
                color = ColorProvider(if (item.done) Muted else Ink),
                fontSize = 13.sp,
                textDecoration = if (item.done) TextDecoration.LineThrough else TextDecoration.None,
            ),
        )
    }
}

private fun openAppIntent(context: Context) =
    Intent(context, MainActivity::class.java)
        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

/**
 * `data` distinto para que el PendingIntent no colisione con el del cuerpo:
 * los extras no cuentan en Intent.filterEquals().
 */
private fun addIntent(context: Context) =
    Intent(context, MainActivity::class.java)
        .setData(Uri.parse("recado://add"))
        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        .putExtra(MainActivity.EXTRA_OPEN_ADD, true)
