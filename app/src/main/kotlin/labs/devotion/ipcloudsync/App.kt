package labs.devotion.ipcloudsync

import labs.devotion.ipcloudsync.config.Config
import labs.devotion.ipcloudsync.config.ConfigKeys
import labs.devotion.ipcloudsync.ipsyncer.IpSyncer
import labs.devotion.ipcloudsync.logger.Logger

fun main() {
    val logLevel = Config.getEnv(ConfigKeys.LOG_LEVEL)
    Logger.setLevel(logLevel)

    Logger.info("IP CloudSync application started")

    val frequencyMins = Config.getEnv(ConfigKeys.CRON_FREQUENCY_MINS)
    Logger.info("Scheduled IP sync frequency: $frequencyMins minutes")

    val ipSyncer = IpSyncer(frequencyMins.toLong())

    Runtime.getRuntime().addShutdownHook(
        Thread {
            Logger.info("Shutdown signal received. Stopping cron job...")
            ipSyncer.stop()
            Logger.info("Cron job stopped. Resources cleaned up.")
            Thread.currentThread().interrupt()
            // TODO: Exit with code 0
        }
    )

    ipSyncer.start()

    Logger.info("Application is running. Press Ctrl+C to exit.")
}
