package com.cyrillrx.logger

// TODO(#41): write to os_log, with the app's bundle id as subsystem and the tag as category.
internal actual fun writeToConsole(entry: LogEntry, body: String) {
    println(LogHelper.formatLogWithDate(entry, body))
}
