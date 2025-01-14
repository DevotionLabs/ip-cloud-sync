package labs.devotion.ipcloudsync.config

import kotlin.test.Test
import kotlin.test.assertFailsWith

class ConfigTest {

    @Test
    fun `should throw an error on non-existing env`() {
        assertFailsWith<IllegalStateException> {
            Config.getEnv(ConfigKeys.CLOUDFLARE_API_TOKEN)
        }
    }
}
