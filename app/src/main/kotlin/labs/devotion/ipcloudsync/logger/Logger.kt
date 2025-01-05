package labs.devotion.ipcloudsync.logger

import java.time.LocalDateTime

object Logger {

    enum class LogLevel(val color: Color) {
        INFO(Color.BLUE),
        WARNING(Color.YELLOW),
        ERROR(Color.RED);
    }

    fun info(message: String) {
        log(message, LogLevel.INFO)
    }

    fun warning(message: String) {
        log(message, LogLevel.WARNING)
    }

    fun error(message: String) {
        log(message, LogLevel.ERROR)
    }

    private fun log(message: String, level: LogLevel = LogLevel.INFO) {
        val timestamp = LocalDateTime.now()
        println("${level.color}[$timestamp] [${level.name}] $message ${Color.RESET}")
    }
}
