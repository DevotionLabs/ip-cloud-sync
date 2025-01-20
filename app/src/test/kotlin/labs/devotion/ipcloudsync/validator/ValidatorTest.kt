package labs.devotion.ipcloudsync.validator

import labs.devotion.ipcloudsync.cloudflareclient.CloudflareClient
import labs.devotion.ipcloudsync.cloudflareclient.CloudflareDnsService
import labs.devotion.ipcloudsync.cloudflareclient.CloudflareZoneService
import labs.devotion.ipcloudsync.httpclient.HttpClient
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFailsWith

class ValidatorTest {

    private lateinit var client: CloudflareClient

    @BeforeTest
    fun setUp() {
        val httpClient = HttpClient("http://test.com", "test-token")
        val zoneService = CloudflareZoneService(httpClient)
        val dnsService = CloudflareDnsService(zoneService, httpClient)

        client = CloudflareClient(dnsService)
    }

    @Test
    fun `should throw an error on trying to resolve a domain containing special characters`() {
        assertFailsWith<IllegalArgumentException> {
            client.fetchDomainProxiedIp("exa_mple.com")
        }

        assertFailsWith<IllegalArgumentException> {
            client.fetchDomainProxiedIp("exa!mple.com")
        }
    }

    @Test
    fun `should throw an error on trying to resolve a domain with invalid structure`() {
        assertFailsWith<IllegalArgumentException> {
            client.fetchDomainProxiedIp("-example.com")
        }

        assertFailsWith<IllegalArgumentException> {
            client.fetchDomainProxiedIp("example-.com")
        }

        assertFailsWith<IllegalArgumentException> {
            client.fetchDomainProxiedIp("example..com")
        }
    }

    @Test
    fun `should throw an error on trying to resolve a domain with an invalid top-level domain`() {
        assertFailsWith<IllegalArgumentException> {
            client.fetchDomainProxiedIp("example.invalidtld")
        }

        assertFailsWith<IllegalArgumentException> {
            client.fetchDomainProxiedIp("example.123")
        }
    }

    @Test
    fun `should throw an error on trying to resolve a domain without a top-level domain`() {
        assertFailsWith<IllegalArgumentException> {
            client.fetchDomainProxiedIp("example")
        }

        assertFailsWith<IllegalArgumentException> {
            client.fetchDomainProxiedIp("www")
        }
    }

    @Test
    fun `should throw an error on trying to resolve a domain containing spaces`() {
        assertFailsWith<IllegalArgumentException> {
            client.fetchDomainProxiedIp("exa mple.com")
        }

        assertFailsWith<IllegalArgumentException> {
            client.fetchDomainProxiedIp("example .com")
        }
    }
}
