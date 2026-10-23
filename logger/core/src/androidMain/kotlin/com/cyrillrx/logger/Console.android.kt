package com.cyrillrx.logger

// TODO(#40): write to Logcat with the native priority, split under its 4,000-character limit.
internal actual fun writeToConsole(entry: LogEntry, body: String) {
    println(LogHelper.formatLogWithDate(entry, body))
}
