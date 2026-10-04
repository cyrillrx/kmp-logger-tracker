package com.cyrillrx.logger

import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LoggerTest {
    @AfterTest
    fun releaseLogger() {
        Logger.release()
    }

    @Test
    fun `dispatches the entry to children that accept its severity`() {
        val child = RamLogChild(minSeverity = Severity.INFO)
        Logger.addChild(child)

        Logger.error(TAG, "message")

        val entry = child.entries.single()
        assertEquals(Severity.ERROR, entry.severity)
        assertEquals(TAG, entry.tag)
        assertEquals("message", entry.message)
    }

    @Test
    fun `skips children that reject the severity`() {
        val child = RamLogChild(minSeverity = Severity.ERROR)
        Logger.addChild(child)

        Logger.debug(TAG, "message")

        assertTrue(child.entries.isEmpty())
    }

    private companion object {
        const val TAG = "LoggerTest"
    }
}
