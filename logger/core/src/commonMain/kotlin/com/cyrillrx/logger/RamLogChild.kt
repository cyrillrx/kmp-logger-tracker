package com.cyrillrx.logger

class RamLogChild(minSeverity: Severity = Severity.VERBOSE) : SeverityLogChild(minSeverity) {
    private val recorded = mutableListOf<LogEntry>()

    val entries: List<LogEntry>
        get() = recorded.toList()

    override fun log(entry: LogEntry) {
        recorded += entry
    }
}
