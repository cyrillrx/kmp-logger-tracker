package com.cyrillrx.logger

import kotlinx.atomicfu.atomic
import kotlinx.atomicfu.update

class RamLogChild(minSeverity: Severity = Severity.VERBOSE) : SeverityLogChild(minSeverity) {
    private val recorded = atomic(emptyList<LogEntry>())

    fun entries(): List<LogEntry> = recorded.value

    override fun log(entry: LogEntry) {
        recorded.update { it + entry }
    }
}
