package labs.devotion.ipcloudsync.config

object Config {

    private var environment: Map<String, String> = System.getenv()

    fun setEnvironmentMap(environment: Map<String, String>) {
        this.environment = environment
    }

    fun getEnv(configKey: ConfigKeys): String {
        return environment[configKey.key] ?: configKey.defaultValue
            ?: throw IllegalStateException("Environment variable ${configKey.key} is not set and no default value was provided.")
    }

    fun getIntEnv(configKey: ConfigKeys): Int {
        val envValue = getEnv(configKey)

        try {
            return envValue.toInt()
        } catch (e: NumberFormatException) {
            throw NumberFormatException("Could not parse environment variable ${configKey.key} (value: $envValue) to an integer.")
        }
    }
}
