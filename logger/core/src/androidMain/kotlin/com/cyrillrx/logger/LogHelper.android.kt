package com.cyrillrx.logger

private const val LOGGER_PACKAGE = "com.cyrillrx.logger."
private val ENTRY_POINT_CLASSES = setOf(
    "com.cyrillrx.logger.Logger",
    "com.cyrillrx.logger.LoggerKt",
    "com.cyrillrx.logger.Log",
)

actual fun getLinkToCurrentMethod(): String? {
    val currentThread = Thread.currentThread()
    val stackTrace: Array<StackTraceElement?>? = currentThread.stackTrace
    val trace = stackTrace?.findRelevantTrace() ?: return null

    return "${trace.linkableMethod()} [thread: ${currentThread.name}]"
}

internal fun Array<StackTraceElement?>.findRelevantTrace(): StackTraceElement? {
    val entryPointIndex = indexOfFirst { it?.className in ENTRY_POINT_CLASSES }
    if (entryPointIndex != -1) return firstOutsideLoggerAfter(entryPointIndex)

    val lastLoggerIndex = indexOfLast { it.isLoggerFrame() }
    if (lastLoggerIndex == -1) return null

    return drop(lastLoggerIndex + 1).firstOrNull { it != null }
}

private fun Array<StackTraceElement?>.firstOutsideLoggerAfter(index: Int): StackTraceElement? =
    drop(index + 1).firstOrNull { it != null && !it.isLoggerFrame() }

private fun StackTraceElement?.isLoggerFrame(): Boolean = this?.className?.startsWith(LOGGER_PACKAGE) == true

private fun StackTraceElement?.linkableMethod(): String {
    if (this == null) return "trace is null"
    return "$className.$methodName($fileName:$lineNumber)"
}

private fun StackTraceElement?.linkableLine(): String {
    if (this == null) return "trace is null"
    return "($fileName:$lineNumber)"
}
