package labs.devotion.ipcloudsync.logger

import java.time.LocalDateTime

object Logger {
    private var userLevel: LogLevel = LogLevel.INFO

    fun setLevel(userLevel: String) {
        this.userLevel = LogLevel.fromString(userLevel)
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

        if (msgLevel.isAtLeast(userLevel)) {
            println("${msgLevel.color}[$timestamp] [${msgLevel.name}] $message ${Color.RESET}")
        }
    }
}
