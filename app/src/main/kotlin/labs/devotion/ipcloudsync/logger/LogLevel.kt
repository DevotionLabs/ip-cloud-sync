package labs.devotion.ipcloudsync.logger

enum class LogLevel(internal val color: Color) {
    DEBUG(Color.GRAY), INFO(Color.BLUE), WARN(Color.YELLOW), ERROR(Color.RED);

    companion object {
        fun fromString(requestedLevel: String): LogLevel {
            return findEntryByValue(requestedLevel) ?: INFO.also {
                Logger.warn("Could not parse log level ($requestedLevel). Using $INFO instead.")
            }
        }

        private fun findEntryByValue(level: String): LogLevel? {
            return entries.find {
                it.name.equals(level, ignoreCase = true)
            }
        }
    }

    fun isAtLeast(level: LogLevel): Boolean {
        return this.ordinal >= level.ordinal
    }
}
