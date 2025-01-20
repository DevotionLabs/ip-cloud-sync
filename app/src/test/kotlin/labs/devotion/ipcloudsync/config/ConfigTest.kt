package labs.devotion.ipcloudsync.config

import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ConfigTest {

    @AfterTest
    fun resetSystemEnvironment() {
        Config.setEnvironmentMap(System.getenv())
    }

    @Test
    fun `should throw an error on non-existing env`() {
        assertFailsWith<IllegalStateException> {
            Config.getEnv(ConfigKeys.CLOUDFLARE_API_TOKEN)
        }
    }

    @Test
    fun `should return the value of a defined env`() {
        val tokenConfig = ConfigKeys.CLOUDFLARE_API_TOKEN
        val expectedToken = "test-token"

        val environment = mapOf(
            tokenConfig.key to expectedToken
        )

        Config.setEnvironmentMap(environment)

        val actualToken = Config.getEnv(tokenConfig)

        assertEquals(expectedToken, actualToken)
    }

    @Test
    fun `should return the default value for a non-overridden env`() {
        val logLevel = ConfigKeys.LOG_LEVEL

        val logLevelValue = Config.getEnv(logLevel)

        assertEquals(logLevel.defaultValue, logLevelValue)
    }
}
