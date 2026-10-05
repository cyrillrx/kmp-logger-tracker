package com.cyrillrx.logger

import kotlinx.atomicfu.atomic

object Log : Logger {
    private val delegate = atomic<Logger>(CompositeLogger())

    fun install(logger: Logger) {
        delegate.value = logger
    }

    override fun isLoggable(severity: Severity, tag: String): Boolean = delegate.value.isLoggable(severity, tag)

    override fun log(
        severity: Severity,
        tag: String,
        throwable: Throwable?,
        attributes: Map<String, String>,
        message: () -> String,
    ) = delegate.value.log(severity, tag, throwable, attributes, message)
}
