package labs.devotion.ipcloudsync.config

object Config {

    fun getEnv(configKey: ConfigKeys): String {
        val environment = System.getenv()

        return environment[configKey.key] ?: configKey.defaultValue
            ?: throw IllegalStateException("Environment variable '\${configKey.key}' is not set and no default value was provided.")
    }
}
