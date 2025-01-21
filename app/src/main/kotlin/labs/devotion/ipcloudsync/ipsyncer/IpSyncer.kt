package labs.devotion.ipcloudsync.ipsyncer

import labs.devotion.ipcloudsync.logger.Logger
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class IpSyncer(private val frequencyInMins: Long) {

    private val scheduler = Executors.newScheduledThreadPool(1)

    fun start() {
        Logger.info("Scheduling IP synchronization task to run every $frequencyInMins minute(s).")
        try {
            scheduleSyncTaskCron()
        } catch (e: Exception) {
            Logger.error("Could not schedule IP synchronization task: ${e.message}")
            throw e
        }
    }

    private fun scheduleSyncTaskCron() {
        scheduler.scheduleAtFixedRate(SyncTask, 0, frequencyInMins, TimeUnit.MINUTES)
    }

    fun stop() {
        Logger.info("Shutting down IP synchronization task")

        try {
            scheduler.shutdown()
        } catch (e: Exception) {
            Logger.error("Could not stop IP synchronization task: ${e.message}")
            throw e
        }
    }
}
