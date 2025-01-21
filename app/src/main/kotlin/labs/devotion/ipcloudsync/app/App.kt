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
        setupLogger()
        Logger.info("IP CloudSync application started")

        try {
            initIpSyncer()

            handleTerminationSignals()

            ipSyncer.start()
            Logger.info("Application is running. Press Ctrl+C to exit.")
        } catch (e: Exception) {
            Logger.error("Application could not be started: ${e.message}")
            exitProcess(1)
        }
    }

    private fun setupLogger() {
        val logLevel = Config.getEnv(ConfigKeys.LOG_LEVEL)
        Logger.setLevel(logLevel)
    }

    private fun initIpSyncer() {
        val frequencyMinsEnvValue = Config.getEnv(ConfigKeys.CRON_FREQUENCY_MINS)

        // TODO: Extract this to another function
        val frequencyMins = try {
            frequencyMinsEnvValue.toLong()
        } catch (e: NumberFormatException) {
            ConfigKeys.CRON_FREQUENCY_MINS.defaultValue?.toLongOrNull()
                ?: throw NumberFormatException("Default frequency value must be a number")
        }

        ipSyncer = IpSyncer(frequencyMins)
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
