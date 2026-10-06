package com.cyrillrx.logger

// Prefix of Logger, LoggerKt, Logger$DefaultImpls and Logger$-CC, which D8 generates when desugaring for minSdk < 24.
private val LOGGER_CLASS_NAME = Logger::class.java.name
private val LOG_CLASS_NAME = Log::class.java.name
private val LOGGER_PACKAGE = LOGGER_CLASS_NAME.substringBeforeLast('.') + "."

actual fun getLinkToCurrentMethod(): String? {
    val currentThread = Thread.currentThread()
    val stackTrace: Array<StackTraceElement?>? = currentThread.stackTrace
    val trace = stackTrace?.findRelevantTrace() ?: return null

    return "${trace.linkableMethod()} [thread: ${currentThread.name}]"
}

// TODO(#34): skip consumer loggers that forward to the Log facade instead of linking to them.
internal fun Array<StackTraceElement?>.findRelevantTrace(): StackTraceElement? {
    val entryPointIndex = indexOfFirst { it.isEntryPoint() }
    if (entryPointIndex != -1) return firstOutsideLoggerAfter(entryPointIndex)

    val lastLoggerIndex = indexOfLast { it.isLoggerFrame() }
    if (lastLoggerIndex == -1) return null

    return drop(lastLoggerIndex + 1).firstOrNull { it != null }
}

private fun Array<StackTraceElement?>.firstOutsideLoggerAfter(index: Int): StackTraceElement? =
    drop(index + 1).firstOrNull { it != null && !it.isLoggerFrame() }

private fun StackTraceElement?.isEntryPoint(): Boolean {
    val className = this?.className ?: return false
    return className == LOG_CLASS_NAME || className.startsWith(LOGGER_CLASS_NAME)
}

private fun StackTraceElement?.isLoggerFrame(): Boolean = this?.className?.startsWith(LOGGER_PACKAGE) == true

private fun StackTraceElement?.linkableMethod(): String {
    if (this == null) return "trace is null"
    return "$className.$methodName($fileName:$lineNumber)"
}
