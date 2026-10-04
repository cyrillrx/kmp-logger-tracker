package com.cyrillrx.logger

private const val LOGGER_PACKAGE = "com.cyrillrx.logger."

actual fun getLinkToCurrentMethod(): String? {
    val currentThread = Thread.currentThread()
    val stackTrace: Array<StackTraceElement?>? = currentThread.stackTrace
    val trace = stackTrace?.findRelevantTrace() ?: return null

    return "${trace.linkableMethod()} [thread: ${currentThread.name}]"
}

internal fun Array<StackTraceElement?>.findRelevantTrace(): StackTraceElement? {
    val lastLoggerIndex = indexOfLast { it?.className?.startsWith(LOGGER_PACKAGE) == true }
    if (lastLoggerIndex == -1) return null

    return drop(lastLoggerIndex + 1).firstOrNull { it != null }
}

private fun StackTraceElement?.linkableMethod(): String {
    if (this == null) return "trace is null"
    return "$className.$methodName($fileName:$lineNumber)"
}

private fun StackTraceElement?.linkableLine(): String {
    if (this == null) return "trace is null"
    return "($fileName:$lineNumber)"
}
