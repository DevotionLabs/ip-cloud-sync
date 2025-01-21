package labs.devotion.ipcloudsync.app

import labs.devotion.ipcloudsync.config.Config
import labs.devotion.ipcloudsync.config.ConfigKeys
import labs.devotion.ipcloudsync.ipsyncer.IpSyncer
import labs.devotion.ipcloudsync.logger.Logger
import kotlin.system.exitProcess

object App {
    private val ipSyncer: IpSyncer by lazy {
        val frequencyMins = Config.getIntEnv(ConfigKeys.CRON_FREQUENCY_MINS)
        IpSyncer(frequencyMins)
    }

    fun start() {
        Logger.applyEnvConfig()
        Logger.info("IP CloudSync application started")

        try {
            handleShutdownSignals()

            ipSyncer.start()
            Logger.info("Application is running. Press Ctrl+C to exit.")
        } catch (e: Exception) {
            Logger.error("Application could not be started: ${e.message}")
            exitProcess(1)
        }
    }

    private fun handleShutdownSignals() {
        val shutdownSignals = listOf("INT", "TERM", "QUIT")

        SignalHandler.register(shutdownSignals) {
            shutdown()
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
