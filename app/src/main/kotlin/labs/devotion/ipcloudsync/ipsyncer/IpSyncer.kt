package labs.devotion.ipcloudsync.ipsyncer

import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class IpSyncer(private val frequencyInMins: Long) {

    private val scheduler = Executors.newScheduledThreadPool(1)

    fun start() {
        scheduler.scheduleAtFixedRate(SyncTask, 0, frequencyInMins, TimeUnit.MINUTES)
    }

    fun stop() {
        scheduler.shutdown()
    }
}
