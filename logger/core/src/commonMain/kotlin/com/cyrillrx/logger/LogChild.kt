package com.cyrillrx.logger

public interface LogChild {
    public fun isLoggable(severity: Severity, tag: String): Boolean = true

    public fun log(entry: LogEntry)
}
