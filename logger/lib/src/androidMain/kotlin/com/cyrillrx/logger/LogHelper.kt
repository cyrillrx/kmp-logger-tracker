package com.cyrillrx.logger

private val LOGGER_CLASS_NAME = Logger::class.java.name

actual fun getLinkToCurrentMethod(): String? {
    val currentThread = Thread.currentThread()
    val stackTrace: Array<StackTraceElement?>? = currentThread.stackTrace
    val trace = stackTrace?.findRelevantTrace() ?: return null

    return "${trace.linkableMethod()} [thread: ${currentThread.name}]"
}

private fun Array<StackTraceElement?>.findRelevantTrace(): StackTraceElement? {
    var lastWasLoggerClass = false

    for (trace in this) {
        trace ?: continue

        val isLoggerClass = trace.className.startsWith(LOGGER_CLASS_NAME)
        if (lastWasLoggerClass && !isLoggerClass) {
            return trace
        }
        lastWasLoggerClass = isLoggerClass
    }
    return null
}

private fun StackTraceElement?.linkableMethod(): String {
    if (this == null) return "trace is null"
    return "${className}.${methodName}(${fileName}:${lineNumber})"
}

private fun StackTraceElement?.linkableLine(): String {
    if (this == null) return "trace is null"
    return "($fileName:$lineNumber)"
}
