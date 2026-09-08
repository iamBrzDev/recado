package app.recado

import android.content.Context
import androidx.glance.appwidget.updateAll
import app.recado.data.ItemRepository
import app.recado.data.RecadoDatabase
import app.recado.widget.RecadoWidget

/**
 * Único sitio donde se arma el repositorio. Lo usan la app y el worker de purga,
 * y es lo que engancha el refresco del widget a cada escritura en Room.
 */
object RecadoGraph {

    fun repository(context: Context): ItemRepository {
        val app = context.applicationContext
        return ItemRepository(RecadoDatabase.get(app).itemDao()) {
            RecadoWidget().updateAll(app)
        }
    }
}
