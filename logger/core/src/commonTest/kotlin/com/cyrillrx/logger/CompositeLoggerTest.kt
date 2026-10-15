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
    fun `keeps the attributes the entry was logged with`() {
        val child = RamLogChild()
        val logger = CompositeLogger(listOf(child))
        val attributes = mutableMapOf("id" to "1")

        logger.log(Severity.INFO, TAG, attributes = attributes) { "message" }
        attributes["id"] = "2"

        assertEquals(mapOf("id" to "1"), child.entries().single().attributes)
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
    fun `logs a placeholder when the message cannot be built`() {
        val child = RamLogChild()
        val logger = CompositeLogger(listOf(child))

        logger.log(Severity.INFO, TAG) { throw IllegalStateException("secret") }

        assertEquals("Failed to build the log message: IllegalStateException", child.entries().single().message)
    }

    @Test
    fun `logs a placeholder when the message failure cannot be described`() {
        val child = RamLogChild()
        val logger = CompositeLogger(listOf(child))

        logger.log(Severity.INFO, TAG) { throw UndescribableException() }

        assertEquals("Failed to build the log message: UndescribableException", child.entries().single().message)
    }

    @Test
    fun `logs a placeholder when the message fails with an anonymous exception`() {
        val child = RamLogChild()
        val logger = CompositeLogger(listOf(child))

        logger.log(Severity.INFO, TAG) { throw object : RuntimeException() {} }

        assertEquals("Failed to build the log message: an anonymous exception", child.entries().single().message)
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
    fun `skips and reports a child whose filter fails`() {
        val failing = FailingFilterLogChild()
        val healthy = RamLogChild()
        val reported = mutableListOf<LogChild>()
        val logger = CompositeLogger(listOf(failing, healthy)) { child, _ -> reported += child }

        logger.log(Severity.INFO, TAG) { "message" }

        assertEquals(1, healthy.entries().size)
        assertEquals(listOf<LogChild>(failing), reported)
    }

    @Test
    fun `isolates a failing child whose equality breaks once registered`() {
        val unhashable = UnhashableFailingLogChild()
        val healthy = RamLogChild()
        val logger = CompositeLogger(listOf(unhashable, healthy))
        unhashable.broken = true

        logger.log(Severity.INFO, TAG) { "message" }

        assertEquals(1, healthy.entries().size)
    }

    @Test
    fun `reports each failure of a child whose equality breaks once registered`() {
        val unhashable = UnhashableFailingLogChild()
        val reported = mutableListOf<LogChild>()
        val logger = CompositeLogger(listOf(unhashable)) { child, _ -> reported += child }
        unhashable.broken = true

        logger.log(Severity.INFO, TAG) { "first" }
        logger.log(Severity.INFO, TAG) { "second" }

        assertEquals(2, reported.size)
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
    fun `removes a child while another child's equality is broken`() {
        val unhashable = UnhashableFailingLogChild()
        val removed = RamLogChild()
        val logger = CompositeLogger(listOf(unhashable, removed))
        unhashable.broken = true

        logger.remove(removed)
        logger.log(Severity.INFO, TAG) { "message" }

        assertTrue(removed.entries().isEmpty())
    }

    @Test
    fun `dispatches to each of two equal children`() {
        val first = EqualLogChild()
        val second = EqualLogChild()
        val logger = CompositeLogger(listOf(first))
        logger.add(second)

        logger.log(Severity.INFO, TAG) { "message" }

        assertEquals(1, first.entries.size)
        assertEquals(1, second.entries.size)
    }

    @Test
    fun `lets a child register another child while it logs`() {
        val added = RamLogChild()
        val logger = CompositeLogger()
        logger.add(RegisteringLogChild { logger.add(added) })

        logger.log(Severity.INFO, TAG) { "first" }
        logger.log(Severity.INFO, TAG) { "second" }

        assertEquals(listOf("second"), added.entries().map { it.message })
    }

    @Test
    fun `lets a child remove itself while it logs`() {
        val logger = CompositeLogger()
        lateinit var removing: RegisteringLogChild
        removing = RegisteringLogChild { logger.remove(removing) }
        logger.add(removing)

        logger.log(Severity.INFO, TAG) { "first" }
        logger.log(Severity.INFO, TAG) { "second" }

        assertEquals(1, removing.calls)
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

    private class UnhashableFailingLogChild : LogChild {
        var broken = false

        override fun log(entry: LogEntry) = error("failing child")

        override fun equals(other: Any?): Boolean = if (broken) error("failing equality") else this === other

        override fun hashCode(): Int = if (broken) error("failing hash") else 0
    }

    private class EqualLogChild : LogChild {
        val entries = mutableListOf<LogEntry>()

        override fun log(entry: LogEntry) {
            entries += entry
        }

        override fun equals(other: Any?): Boolean = other is EqualLogChild

        override fun hashCode(): Int = 0
    }

    private class FailingFilterLogChild : LogChild {
        override fun isLoggable(severity: Severity, tag: String): Boolean = error("failing filter")

        override fun log(entry: LogEntry) = Unit
    }

    private class RegisteringLogChild(private val register: () -> Unit) : LogChild {
        var calls = 0
            private set

        override fun log(entry: LogEntry) {
            calls++
            register()
        }
    }

    private class UndescribableException : RuntimeException() {
        override val message: String get() = error("no message")

        override fun toString(): String = error("no description")
    }

    private companion object {
        const val TAG = "CompositeLoggerTest"
    }
}
