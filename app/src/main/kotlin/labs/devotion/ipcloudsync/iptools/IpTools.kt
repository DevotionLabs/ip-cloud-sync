package labs.devotion.ipcloudsync.iptools

import java.net.InetAddress

object IpTools {
    fun validateIp(ip: String): String {
        require(isValidIp(ip)) { "Invalid IP format: $ip" }
        return ip
    }

    fun isValidIp(ip: String): Boolean {
        return try {
            InetAddress.getByName(ip)
            true
        } catch (e: Exception) {
            false
        }
    }
}
