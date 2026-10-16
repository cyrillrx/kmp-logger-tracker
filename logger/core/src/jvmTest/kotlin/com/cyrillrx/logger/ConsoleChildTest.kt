package com.cyrillrx.logger

import com.example.console.ConsoleCaller
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Instant

class ConsoleChildTest {
    private val standardOutput = System.out
    private val standardError = System.err
    private val output = ByteArrayOutputStream()
    private val error = ByteArrayOutputStream()

    @BeforeTest
    fun captureOutput() {
        System.setOut(PrintStream(output, true))
        System.setErr(PrintStream(error, true))
    }

    @AfterTest
    fun restoreOutput() {
        System.setOut(standardOutput)
        System.setErr(standardError)
    }

    @Test
    fun `prints an entry once with its timestamp severity and tag`() {
        ConsoleChild().log(entryOf())

        assertEquals(listOf("$PREFIX - retrying"), printedLines())
    }

    @Test
    fun `prints the attributes after the message`() {
        ConsoleChild().log(entryOf(attributes = mapOf("requestId" to "42")))

        assertEquals(listOf("$PREFIX - retrying {requestId=42}"), printedLines())
    }

    @Test
    fun `prints the stack trace of an entry with a throwable`() {
        val failure = IllegalStateException("sync failed")

        ConsoleChild().log(entryOf(throwable = failure))

        assertEquals("$PREFIX - retrying\n${failure.stackTraceToString()}".lines(), printedLines())
    }

    @Test
    fun `prints errors on the standard error`() {
        ConsoleChild().log(entryOf(severity = Severity.ERROR))

        assertEquals(listOf("$TIMESTAMP - ERROR - $TAG - retrying"), printedLines(error))
        assertTrue(printedLines().isEmpty())
    }

    @Test
    fun `links to the logging method when logs are clickable`() {
        ConsoleCaller().logWarning()

        val link = printedLines().last()
        assertTrue(link.startsWith("com.example.console.ConsoleCaller.logWarning(ConsoleCaller.kt:"), link)
        assertTrue(link.endsWith("[thread: ${Thread.currentThread().name}]"), link)
    }

    private fun printedLines(stream: ByteArrayOutputStream = output): List<String> =
        stream.toString().lines().dropLast(1)

    private fun entryOf(
        severity: Severity = Severity.WARN,
        throwable: Throwable? = null,
        attributes: Map<String, String> = emptyMap(),
    ) = LogEntry(
        severity = severity,
        tag = TAG,
        message = "retrying",
        throwable = throwable,
        attributes = attributes,
        timestamp = Instant.parse(TIMESTAMP),
    )

    private companion object {
        const val TIMESTAMP = "2026-10-04T10:00:00Z"
        const val TAG = "Sync"
        const val PREFIX = "$TIMESTAMP - WARN - $TAG"
    }
}
