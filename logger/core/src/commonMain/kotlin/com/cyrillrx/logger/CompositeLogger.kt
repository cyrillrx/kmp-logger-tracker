package com.cyrillrx.logger

import kotlinx.atomicfu.atomic
import kotlinx.atomicfu.update

class CompositeLogger(
    children: List<LogChild> = emptyList(),
    private val onChildError: (LogChild, Throwable) -> Unit = { _, _ -> },
) : Logger {
    private val children = atomic(children.distinct())

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

        val entry = LogEntry(severity, tag, message(), throwable, attributes)
        loggableChildren.forEach { child ->
            runCatching { child.log(entry) }.onFailure { report(child, it) }
        }
    }

    private fun LogChild.isLoggableSafely(severity: Severity, tag: String): Boolean =
        runCatching { isLoggable(severity, tag) }
            .onFailure { report(this, it) }
            .getOrDefault(false)

    private fun report(child: LogChild, error: Throwable) {
        runCatching { onChildError(child, error) }
    }
}
