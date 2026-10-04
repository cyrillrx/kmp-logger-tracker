package com.cyrillrx.logger

class RamLogChild(minSeverity: Severity = Severity.VERBOSE) : SeverityLogChild(minSeverity) {
    private val recorded = mutableListOf<LogEntry>()

    fun entries(): List<LogEntry> = recorded.toList()

    override fun log(entry: LogEntry) {
        recorded += entry
    }
}
