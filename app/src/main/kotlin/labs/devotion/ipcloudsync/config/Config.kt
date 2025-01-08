object Config {

    private val environment: Map<String, String> = System.getenv()

    enum class ConfigKey(val key: String) {
        IP_FETCH_SERVER_URL("IPCLOUDSYNC_IP_FETCH_SERVER_URL");
    }

    fun getEnv(configKey: ConfigKey, default: String? = null): String {
        return environment[configKey.key] ?: default
            ?: throw IllegalStateException("Environment variable '\${configKey.key}' is not set and no default value was provided.")
    }
}
