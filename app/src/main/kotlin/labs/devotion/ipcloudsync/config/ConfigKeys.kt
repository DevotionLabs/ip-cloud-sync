package labs.devotion.ipcloudsync.config

enum class ConfigKeys(val key: String, val defaultValue: String? = null) {
    CLOUDFLARE_API_URL("IPCLOUDSYNC_CLOUDFLARE_API_URL", "https://api.cloudflare.com"),
    CLOUDFLARE_API_TOKEN("IPCLOUDSYNC_CLOUDFLARE_API_TOKEN"),
    CRON_FREQUENCY_MINS("IPCLOUDSYNC_CRON_FREQUENCY_MINS", "60"),
    DOMAIN("IPCLOUDSYNC_DOMAIN"),
    IP_FETCH_SERVER_URL("IPCLOUDSYNC_IP_FETCH_SERVER_URL", "https://api.ipify.org");
}
