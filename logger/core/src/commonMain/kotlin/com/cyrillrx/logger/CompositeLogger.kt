package com.cyrillrx.logger

import kotlinx.atomicfu.atomic
import kotlinx.atomicfu.update

class CompositeLogger(
    children: List<LogChild> = emptyList(),
    private val onChildError: (LogChild, Throwable) -> Unit = { _, _ -> },
) : Logger {
    private val children = atomic(children)

    fun add(child: LogChild) {
        children.update { it + child }
    }

    fun remove(child: LogChild) {
        children.update { it - child }
    }

    override fun isLoggable(severity: Severity, tag: String): Boolean =
        children.value.any { it.acceptsSafely(severity, tag) }

    override fun log(
        severity: Severity,
        tag: String,
        throwable: Throwable?,
        attributes: Map<String, String>,
        message: () -> String,
    ) {
        val accepting = children.value.filter { it.acceptsSafely(severity, tag) }
        if (accepting.isEmpty()) return

        val entry = LogEntry(severity, tag, message(), throwable, attributes)
        accepting.forEach { child ->
            runCatching { child.log(entry) }.onFailure { report(child, it) }
        }
    }

    private fun LogChild.acceptsSafely(severity: Severity, tag: String): Boolean =
        runCatching { isLoggable(severity, tag) }
            .onFailure { report(this, it) }
            .getOrDefault(false)

    private fun report(child: LogChild, error: Throwable) {
        runCatching { onChildError(child, error) }
    }
}
