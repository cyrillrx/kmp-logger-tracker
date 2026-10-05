package com.cyrillrx.logger

import kotlinx.atomicfu.atomic
import kotlinx.atomicfu.getAndUpdate
import kotlinx.atomicfu.update

/**
 * A message lambda that throws does not reach the caller: children receive a placeholder message naming the failure.
 *
 * @param onChildError called when a child throws. A failure raised while that child's previous failure is still being
 * reported is dropped, so a handler that logs back through this logger cannot recurse.
 */
class CompositeLogger(
    children: List<LogChild> = emptyList(),
    private val onChildError: (LogChild, Throwable) -> Unit = { _, _ -> },
) : Logger {
    private val children = atomic(children.distinct())
    private val reportingChildren = atomic(emptySet<LogChild>())

    fun add(child: LogChild) {
        children.update { if (child in it) it else it + child }
    }

    fun remove(child: LogChild) {
        children.update { it - child }
    }

    override fun isLoggable(severity: Severity, tag: String): Boolean =
        children.value.any { it.isLoggableSafely(severity, tag) }

    override fun log(
        severity: Severity,
        tag: String,
        throwable: Throwable?,
        attributes: Map<String, String>,
        message: () -> String,
    ) {
        val loggableChildren = children.value.filter { it.isLoggableSafely(severity, tag) }
        if (loggableChildren.isEmpty()) return

        val entry = LogEntry(severity, tag, buildMessageSafely(message), throwable, attributes)
        loggableChildren.forEach { child ->
            runCatching { child.log(entry) }.onFailure { report(child, it) }
        }
    }

    private fun buildMessageSafely(message: () -> String): String =
        runCatching(message).getOrElse { "Failed to build the log message: $it" }

    private fun LogChild.isLoggableSafely(severity: Severity, tag: String): Boolean =
        runCatching { isLoggable(severity, tag) }
            .onFailure { report(this, it) }
            .getOrDefault(false)

    private fun report(child: LogChild, error: Throwable) {
        if (!startReporting(child)) return

        try {
            runCatching { onChildError(child, error) }
        } finally {
            reportingChildren.update { it - child }
        }
    }

    // TODO(#33): make the guard per-thread so concurrent failures of the same child are not dropped.
    private fun startReporting(child: LogChild): Boolean {
        val before = reportingChildren.getAndUpdate { it + child }
        return child !in before
    }
}
