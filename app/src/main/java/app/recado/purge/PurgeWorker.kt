package app.recado.purge

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import app.recado.RecadoGraph

/** DELETE de los tachados. Idempotente: si no hay nada, no pasa nada. */
class PurgeWorker(
    appContext: Context,
    params: WorkerParameters,
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        RecadoGraph.repository(applicationContext).clearDone()
        PurgeScheduler.recordPurge(applicationContext)
        return Result.success()
    }
}
