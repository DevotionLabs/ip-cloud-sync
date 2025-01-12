package labs.devotion.ipcloudsync.config

import com.github.stefanbirkner.systemlambda.SystemLambda.withEnvironmentVariable
import labs.devotion.ipcloudsync.logger.LogLevel
import labs.devotion.ipcloudsync.logger.Logger
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ConfigTest {

    @Test
    fun `should set log level based on Config`() {
        withEnvironmentVariable(ConfigKeys.LOG_LEVEL.key, "DEBUG").execute {
            val logLevel = Config.getEnv(ConfigKeys.LOG_LEVEL)

            Logger.setLevel(logLevel)

            assertEquals(LogLevel.DEBUG, Logger.getLevel())
        }

        Logger.setLevel("INFO") // Reset
    }

    @Test
    fun `should throw an error on non-existing env`() {
        assertFailsWith<IllegalStateException> {
            Config.getEnv(ConfigKeys.CLOUDFLARE_API_TOKEN)
        }
    }

    @Test
    fun `should use a default value when it is set`() {
        val logLevelConfig = ConfigKeys.LOG_LEVEL
        withEnvironmentVariable(logLevelConfig.key, null).execute {
            val logLevel = Config.getEnv(logLevelConfig)

            assertEquals(logLevelConfig.defaultValue, logLevel)
        }
    }
}
