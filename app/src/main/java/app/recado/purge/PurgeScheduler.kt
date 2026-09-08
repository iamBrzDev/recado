package app.recado.purge

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.concurrent.TimeUnit

/**
 * Los tachados se borran solos pasada la medianoche. No se archivan.
 */
object PurgeScheduler {

    private const val DAILY_WORK = "recado-purge-daily"
    private const val CATCHUP_WORK = "recado-purge-catchup"
    private const val PREFS = "recado_purge"
    private const val KEY_LAST_PURGE = "last_purge_at"

    /** Ventana pedida: [00:00, 00:30] local. */
    private val WINDOW_END = LocalTime.of(0, 30)
    private const val FLEX_MINUTES = 30L

    /** Al abrir la app: deja la diaria puesta y recupera la medianoche perdida. */
    fun onAppStart(context: Context) {
        scheduleDaily(context)

        val last = prefs(context).getLong(KEY_LAST_PURGE, 0L)
        if (last == 0L) {
            // Primer arranque: no borramos tachados que el usuario nunca vio pasar una medianoche.
            recordPurge(context)
            return
        }
        if (last < startOfTodayMillis()) {
            WorkManager.getInstance(context).enqueueUniqueWork(
                CATCHUP_WORK,
                ExistingWorkPolicy.KEEP,
                OneTimeWorkRequestBuilder<PurgeWorker>().build(),
            )
        }
    }

    /**
     * La ventana flex de WorkManager son los últimos 30 min del periodo, así que
     * arrancando el periodo a las 00:30 cada corrida cae entre 00:00 y 00:30.
     */
    private fun scheduleDaily(context: Context) {
        val request = PeriodicWorkRequestBuilder<PurgeWorker>(
            repeatInterval = 24,
            repeatIntervalTimeUnit = TimeUnit.HOURS,
            flexTimeInterval = FLEX_MINUTES,
            flexTimeIntervalUnit = TimeUnit.MINUTES,
        )
            .setInitialDelay(millisUntil(WINDOW_END), TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            DAILY_WORK,
            ExistingPeriodicWorkPolicy.KEEP,
            request,
        )
    }

    fun recordPurge(context: Context) {
        prefs(context).edit()
            .putLong(KEY_LAST_PURGE, System.currentTimeMillis())
            .apply()
    }

    private fun prefs(context: Context) =
        context.applicationContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

    private fun startOfTodayMillis(): Long =
        LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()

    private fun millisUntil(target: LocalTime): Long {
        val now = ZonedDateTime.now()
        var next = now.toLocalDate().atTime(target).atZone(now.zone)
        if (!next.isAfter(now)) next = next.plusDays(1)
        return Duration.between(now, next).toMillis()
    }
}
