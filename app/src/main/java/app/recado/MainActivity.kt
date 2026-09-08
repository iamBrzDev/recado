package app.recado

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewmodel.compose.viewModel
import app.recado.purge.PurgeScheduler
import app.recado.ui.ItemsViewModel
import app.recado.ui.RecadoApp
import app.recado.ui.theme.RecadoTheme

class MainActivity : ComponentActivity() {

    /** Lo pide el widget con EXTRA_OPEN_ADD; se consume al abrir el sheet. */
    private val openAddRequest = mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
        )

        openAddRequest.value = intent?.getBooleanExtra(EXTRA_OPEN_ADD, false) == true
        PurgeScheduler.onAppStart(applicationContext)

        setContent {
            RecadoTheme {
                val viewModel: ItemsViewModel =
                    viewModel(factory = ItemsViewModel.factory(applicationContext))
                RecadoApp(
                    viewModel = viewModel,
                    openAddRequest = openAddRequest.value,
                    onOpenAddConsumed = { openAddRequest.value = false },
                )
            }
        }
    }

    /** La app ya estaba abierta y el widget pide apuntar algo. */
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        if (intent.getBooleanExtra(EXTRA_OPEN_ADD, false)) {
            openAddRequest.value = true
        }
    }

    companion object {
        const val EXTRA_OPEN_ADD = "openAdd"
    }
}
