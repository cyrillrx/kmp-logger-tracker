package com.cyrillrx.logger

import kotlin.test.Test
import kotlin.test.assertEquals

class LoggerTest {
    @Test
    fun `each shortcut logs at its own severity`() {
        val child = RamLogChild()
        val logger = CompositeLogger(listOf(child))

        logger.verbose(TAG) { "verbose" }
        logger.debug(TAG) { "debug" }
        logger.info(TAG) { "info" }
        logger.warn(TAG) { "warn" }
        logger.error(TAG) { "error" }
        logger.fatal(TAG) { "fatal" }

        assertEquals(Severity.entries, child.entries().map { it.severity })
    }

    private companion object {
        const val TAG = "LoggerTest"
    }
}
