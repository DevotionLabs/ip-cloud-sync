package labs.devotion.ipcloudsync.logger

import labs.devotion.ipcloudsync.config.Config
import labs.devotion.ipcloudsync.config.ConfigKeys
import java.time.LocalDateTime

object Logger {
    private var currentLevel: LogLevel = LogLevel.INFO

    fun applyEnvConfig() {
        val logLevelValue = Config.getEnv(ConfigKeys.LOG_LEVEL)
        this.currentLevel = LogLevel.fromString(logLevelValue)
    }

    fun debug(message: String) {
        log(message, LogLevel.DEBUG)
    }

    fun info(message: String) {
        log(message, LogLevel.INFO)
    }

    fun warn(message: String) {
        log(message, LogLevel.WARN)
    }

    fun error(message: String) {
        log(message, LogLevel.ERROR)
    }

    private fun log(message: String, msgLevel: LogLevel = LogLevel.INFO) {
        val timestamp = LocalDateTime.now()

        if (msgLevel.isAtLeast(currentLevel)) {
            println("${msgLevel.color}[$timestamp] [${msgLevel.name}] $message ${Color.RESET}")
        }
    }
}
