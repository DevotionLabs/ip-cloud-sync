package labs.devotion.ipcloudsync.validator

import labs.devotion.ipcloudsync.logger.Logger
import org.apache.commons.validator.routines.DomainValidator
import java.net.InetAddress

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
        require(isValidDomain(domain)) { "Invalid domain format: $domain" }
    }

    private fun isValidDomain(domain: String): Boolean {
        val domainValidator = DomainValidator.getInstance(false)

        return domainValidator.isValid(domain)
    }
}
