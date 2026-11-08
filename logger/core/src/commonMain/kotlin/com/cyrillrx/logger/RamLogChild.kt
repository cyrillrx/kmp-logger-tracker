package com.cyrillrx.logger

import kotlinx.atomicfu.locks.SynchronizedObject
import kotlinx.atomicfu.locks.synchronized

public class RamLogChild(minSeverity: Severity = Severity.VERBOSE) : SeverityLogChild(minSeverity) {
    private val lock = SynchronizedObject()
    private val recorded = mutableListOf<LogEntry>()

    public fun entries(): List<LogEntry> = synchronized(lock) { recorded.toList() }

    override fun log(entry: LogEntry) {
        synchronized(lock) { recorded += entry }
    }
}
