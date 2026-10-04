package com.cyrillrx.logger

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertSame
import kotlin.test.assertTrue

class CompositeLoggerTest {
    @Test
    fun `dispatches the entry to children that accept its severity`() {
        val child = RamLogChild(minSeverity = Severity.INFO)
        val logger = CompositeLogger(listOf(child))
        val error = IllegalStateException()

        logger.log(Severity.ERROR, TAG, error, mapOf("id" to "42")) { "message" }

        val entry = child.entries().single()
        assertEquals(Severity.ERROR, entry.severity)
        assertEquals(TAG, entry.tag)
        assertEquals("message", entry.message)
        assertSame(error, entry.throwable)
        assertEquals(mapOf("id" to "42"), entry.attributes)
    }

    @Test
    fun `skips children that reject the severity`() {
        val child = RamLogChild(minSeverity = Severity.ERROR)
        val logger = CompositeLogger(listOf(child))

        logger.log(Severity.DEBUG, TAG) { "message" }

        assertTrue(child.entries().isEmpty())
    }

    @Test
    fun `does not build the message when no child accepts the severity`() {
        val logger = CompositeLogger(listOf(RamLogChild(minSeverity = Severity.ERROR)))
        var built = false

        logger.log(Severity.DEBUG, TAG) {
            built = true
            "message"
        }

        assertFalse(built)
    }

    @Test
    fun `builds the message once for several children`() {
        val logger = CompositeLogger(listOf(RamLogChild(), RamLogChild()))
        var builds = 0

        logger.log(Severity.INFO, TAG) {
            builds++
            "message"
        }

        assertEquals(1, builds)
    }

    @Test
    fun `isolates a failing child and reports it`() {
        val failing = FailingLogChild()
        val healthy = RamLogChild()
        val reported = mutableListOf<LogChild>()
        val logger = CompositeLogger(listOf(failing, healthy)) { child, _ -> reported += child }

        logger.log(Severity.INFO, TAG) { "message" }

        assertEquals(1, healthy.entries().size)
        assertEquals(listOf<LogChild>(failing), reported)
    }

    @Test
    fun `swallows a failure of the error handler`() {
        val logger = CompositeLogger(listOf(FailingLogChild())) { _, error -> throw error }

        logger.log(Severity.INFO, TAG) { "message" }
    }

    @Test
    fun `does not recurse when the error handler logs through the same logger`() {
        val failing = FailingLogChild()
        val reported = mutableListOf<LogChild>()
        lateinit var logger: CompositeLogger
        logger = CompositeLogger(listOf(failing)) { child, _ ->
            reported += child
            logger.log(Severity.ERROR, TAG) { "child failed" }
        }

        logger.log(Severity.INFO, TAG) { "message" }

        assertEquals(listOf<LogChild>(failing), reported)
    }

    @Test
    fun `reports a child again once its previous failure is reported`() {
        val failing = FailingLogChild()
        val reported = mutableListOf<LogChild>()
        val logger = CompositeLogger(listOf(failing)) { child, _ -> reported += child }

        logger.log(Severity.INFO, TAG) { "first" }
        logger.log(Severity.INFO, TAG) { "second" }

        assertEquals(listOf<LogChild>(failing, failing), reported)
    }

    @Test
    fun `stops dispatching to a removed child`() {
        val child = RamLogChild()
        val logger = CompositeLogger()
        logger.add(child)

        logger.remove(child)
        logger.log(Severity.INFO, TAG) { "message" }

        assertTrue(child.entries().isEmpty())
    }

    @Test
    fun `dispatches once to a child added twice`() {
        val child = RamLogChild()
        val logger = CompositeLogger()
        logger.add(child)
        logger.add(child)

        logger.log(Severity.INFO, TAG) { "message" }

        assertEquals(1, child.entries().size)
    }

    @Test
    fun `dispatches once to a child passed twice to the constructor`() {
        val child = RamLogChild()
        val logger = CompositeLogger(listOf(child, child))

        logger.log(Severity.INFO, TAG) { "message" }

        assertEquals(1, child.entries().size)
    }

    @Test
    fun `removes a child added twice in a single call`() {
        val child = RamLogChild()
        val logger = CompositeLogger()
        logger.add(child)
        logger.add(child)

        logger.remove(child)
        logger.log(Severity.INFO, TAG) { "message" }

        assertTrue(child.entries().isEmpty())
    }

    @Test
    fun `is loggable when one child accepts the severity`() {
        val logger = CompositeLogger(listOf(RamLogChild(Severity.ERROR), RamLogChild(Severity.DEBUG)))

        assertTrue(logger.isLoggable(Severity.INFO, TAG))
        assertFalse(logger.isLoggable(Severity.VERBOSE, TAG))
    }

    private class FailingLogChild : LogChild {
        override fun log(entry: LogEntry) = error("failing child")
    }

    private companion object {
        const val TAG = "CompositeLoggerTest"
    }
}
