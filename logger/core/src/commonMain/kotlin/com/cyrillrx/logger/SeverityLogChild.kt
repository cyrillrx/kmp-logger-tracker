package com.cyrillrx.logger

public abstract class SeverityLogChild(private val minSeverity: Severity) : LogChild {
    override fun isLoggable(severity: Severity, tag: String): Boolean = severity >= minSeverity
}
