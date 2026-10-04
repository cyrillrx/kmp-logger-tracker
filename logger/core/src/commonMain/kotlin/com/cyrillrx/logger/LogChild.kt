package com.cyrillrx.logger

interface LogChild {
    fun isLoggable(severity: Severity, tag: String): Boolean = true

    fun log(entry: LogEntry)
}
