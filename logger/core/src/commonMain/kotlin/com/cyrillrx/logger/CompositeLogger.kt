package com.cyrillrx.logger

import kotlinx.atomicfu.atomic
import kotlinx.atomicfu.getAndUpdate
import kotlinx.atomicfu.update

/**
 * A message lambda that throws does not reach the caller: children receive a placeholder message naming the failure
 * type only, so the failure's own message never leaves through a child.
 *
 * Children are told apart by identity, so two equal children both receive the entries.
 *
 * @param onChildError called when a child throws. A failure raised while that child's previous failure is still being
 * reported is dropped, so a handler that logs back through this logger cannot recurse.
 */
class CompositeLogger(
    children: List<LogChild> = emptyList(),
    private val onChildError: (LogChild, Throwable) -> Unit = { _, _ -> },
) : Logger {
    private val children = atomic(children.fold(emptyList<LogChild>()) { list, child -> list.withInstance(child) })
    private val reportingChildren = atomic(emptyList<LogChild>())

    fun add(child: LogChild) {
        children.update { it.withInstance(child) }
    }

    fun remove(child: LogChild) {
        children.update { it.withoutInstance(child) }
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
        var entry: LogEntry? = null
        children.value.forEach { child ->
            if (!child.isLoggableSafely(severity, tag)) return@forEach

            val built = entry ?: LogEntry(severity, tag, buildMessageSafely(message), throwable, attributes.toMap())
            entry = built
            runCatching { child.log(built) }.onFailure { report(child, it) }
        }
    }

    private fun buildMessageSafely(message: () -> String): String =
        runCatching(message).getOrElse {
            "Failed to build the log message: ${it::class.simpleName ?: "an anonymous exception"}"
        }

    private fun LogChild.isLoggableSafely(severity: Severity, tag: String): Boolean =
        runCatching { isLoggable(severity, tag) }
            .onFailure { report(this, it) }
            .getOrDefault(false)

    private fun report(child: LogChild, error: Throwable) {
        runCatching { reportOnce(child, error) }
    }

    private fun reportOnce(child: LogChild, error: Throwable) {
        if (!startReporting(child)) return

        runCatching { onChildError(child, error) }
        reportingChildren.update { it.withoutInstance(child) }
    }

    // TODO(#33): make the guard per-thread so concurrent failures of the same child are not dropped.
    private fun startReporting(child: LogChild): Boolean {
        val before = reportingChildren.getAndUpdate { it.withInstance(child) }
        return !before.containsInstance(child)
    }

    // Identity rather than equality: a child's equals may throw or change once it is registered.
    private fun List<LogChild>.containsInstance(child: LogChild): Boolean = any { it === child }

    private fun List<LogChild>.withInstance(child: LogChild): List<LogChild> =
        if (containsInstance(child)) this else this + child

    private fun List<LogChild>.withoutInstance(child: LogChild): List<LogChild> = filterNot { it === child }
}
