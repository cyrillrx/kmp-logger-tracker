package com.cyrillrx.logger

internal actual fun writeToConsole(entry: LogEntry, body: String) {
    val line = LogHelper.formatLogWithDate(entry, body)
    if (entry.severity >= Severity.ERROR) System.err.println(line) else System.out.println(line)
}
