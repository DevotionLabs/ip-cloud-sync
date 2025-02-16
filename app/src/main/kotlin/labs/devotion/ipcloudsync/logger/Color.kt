package labs.devotion.ipcloudsync.logger

internal enum class Color(private val value: String) {
    BLUE("\u001B[34m"),
    YELLOW("\u001B[33m"),
    RED("\u001B[31m"),
    GRAY("\u001B[90m"),
    RESET("\u001B[0m");

    override fun toString(): String {
        return value
    }
}
