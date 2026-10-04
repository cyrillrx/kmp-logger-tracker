package com.cyrillrx.logger

import kotlin.time.Clock
import kotlin.time.Instant

data class LogEntry(
    val severity: Severity,
    val tag: String,
    val message: String,
    val throwable: Throwable? = null,
    val attributes: Map<String, String> = emptyMap(),
    val timestamp: Instant = Clock.System.now(),
)
