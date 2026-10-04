package com.cyrillrx.logger

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RamLogChildTest {
    @Test
    fun `accepts severities at or above its minimum`() {
        val child = RamLogChild(minSeverity = Severity.WARN)

        assertTrue(child.isLoggable(Severity.WARN, TAG))
        assertTrue(child.isLoggable(Severity.FATAL, TAG))
    }

    @Test
    fun `rejects severities below its minimum`() {
        val child = RamLogChild(minSeverity = Severity.WARN)

        assertFalse(child.isLoggable(Severity.INFO, TAG))
    }

    @Test
    fun `records logged entries in order`() {
        val child = RamLogChild()
        val first = LogEntry(Severity.INFO, TAG, "first")
        val second = LogEntry(Severity.ERROR, TAG, "second")

        child.log(first)
        child.log(second)

        assertEquals(listOf(first, second), child.entries())
    }

    private companion object {
        const val TAG = "RamLogChildTest"
    }
}
