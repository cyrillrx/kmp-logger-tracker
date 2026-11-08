package com.cyrillrx.logger

import kotlin.time.Clock
import kotlin.time.Instant

// TODO(#45): settle the data class before the 1.0 ABI freeze, since its copy and componentN signatures are frozen.
public data class LogEntry(
    val severity: Severity,
    val tag: String,
    val message: String,
    val throwable: Throwable? = null,
    val attributes: Map<String, String> = emptyMap(),
    val timestamp: Instant = Clock.System.now(),
)
