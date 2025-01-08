package labs.devotion.ipcloudsync

import labs.devotion.ipcloudsync.config.Config
import labs.devotion.ipcloudsync.config.ConfigKeys
import labs.devotion.ipcloudsync.ipsyncer.IpSyncer
import labs.devotion.ipcloudsync.logger.Logger

fun main() {
    Logger.info("IP CloudSync application started")

    val frequencyMins = Config.getEnv(ConfigKeys.CRON_FREQUENCY_MINS)
    val ipSyncer = IpSyncer(frequencyMins.toLong())

    ipSyncer.start()
}
