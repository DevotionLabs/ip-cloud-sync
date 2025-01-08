package labs.devotion.ipcloudsync.config

object Config {

    private val environment: Map<String, String> = System.getenv()

    fun getEnv(configKey: ConfigKeys): String {
        return environment[configKey.key] ?: configKey.defaultValue
            ?: throw IllegalStateException("Environment variable '\${configKey.key}' is not set and no default value was provided.")
    }
}
