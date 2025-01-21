package labs.devotion.ipcloudsync.app

import labs.devotion.ipcloudsync.config.Config
import labs.devotion.ipcloudsync.config.ConfigKeys
import labs.devotion.ipcloudsync.ipsyncer.IpSyncer
import labs.devotion.ipcloudsync.logger.Logger
import sun.misc.Signal
import kotlin.system.exitProcess

object App {
    private lateinit var ipSyncer: IpSyncer

    fun start() {
        val logLevel = Config.getEnv(ConfigKeys.LOG_LEVEL)
        Logger.setLevel(logLevel)
        Logger.info("IP CloudSync application started")

        val frequencyMins = Config.getEnv(ConfigKeys.CRON_FREQUENCY_MINS)
        ipSyncer = IpSyncer(frequencyMins.toLong())

        handleTerminationSignals()

        try {
            ipSyncer.start()
            Logger.info("Application is running. Press Ctrl+C to exit.")
        } catch (e: Exception) {
            Logger.error("Application could not be started: ${e.message}")
            exitProcess(1)
        }
    }

    private fun handleTerminationSignals() {
        val terminationSignals = listOf("INT", "TERM")
        terminationSignals.forEach { signal ->
            Signal.handle(Signal(signal)) { shutdown() }
        }
    }

    private fun shutdown() {
        try {
            ipSyncer.stop()
            Logger.info("Shutting down application")
            exitProcess(0)
        } catch (e: Exception) {
            Logger.error("Application could not be shut down gracefully: ${e.message}")
            exitProcess(1)
        }
    }
}
