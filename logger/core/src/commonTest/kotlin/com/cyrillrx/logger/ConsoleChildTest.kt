package com.cyrillrx.logger

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame

class ConsoleChildTest {
    private val written = mutableListOf<Pair<LogEntry, String>>()
    private val child = ConsoleChild(Severity.VERBOSE, clickableLogs = false) { entry, body ->
        written += entry to body
    }

    @Test
    fun `writes the message as the body`() {
        val entry = entryOf()

        child.log(entry)

        assertSame(entry, written.single().first)
        assertEquals("retrying", written.single().second)
    }

    @Test
    fun `writes the attributes after the message`() {
        child.log(entryOf(attributes = mapOf("requestId" to "42")))

        assertEquals("retrying {requestId=42}", written.single().second)
    }

    @Test
    fun `writes the stack trace after the message of an entry with a throwable`() {
        val failure = IllegalStateException("sync failed")

        child.log(entryOf(throwable = failure))

        assertEquals("retrying\n${failure.stackTraceToString()}", written.single().second)
    }

    private fun entryOf(throwable: Throwable? = null, attributes: Map<String, String> = emptyMap()) = LogEntry(
        severity = Severity.WARN,
        tag = "Sync",
        message = "retrying",
        throwable = throwable,
        attributes = attributes,
    )
}
