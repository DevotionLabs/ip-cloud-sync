package labs.devotion.ipcloudsync.httpclient

enum class Protocol(val value: String) {
    HTTP("http://"),
    HTTPS("https://");

    override fun toString(): String {
        return value
    }
}