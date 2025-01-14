package labs.devotion.ipcloudsync.validator

import labs.devotion.ipcloudsync.logger.Logger
import java.net.InetAddress

private val DOMAIN_REGEX = Regex("^(?!-)[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*\\.[A-Za-z]{2,63}\$")

object NetworkFormatValidator {
    fun validateIp(ip: String): String {
        Logger.debug("Validating IP format")
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

    fun validateDomain(domain: String) {
        Logger.debug("Validating domain format: $domain")
        require(domain.isNotBlank()) { "Domain must be defined" }
        require(DOMAIN_REGEX.matches(domain)) { "Invalid domain format: $domain" }
    }
}