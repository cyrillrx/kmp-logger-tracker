package com.cyrillrx.logger;

import kotlinx.datetime.Clock

/**
 * @author Cyril Leroux
 *      Created on 03/01/2019.
 */

object LogHelper {
    private val LOGGER_CLASS_NAME = Logger::class.java.name

    fun simpleLog(severity: Severity, tag: String?, message: String?): String {
        return "${currentDateTime()} - ${severity.label} - $tag - $message"
    }

    fun logWithStackTrace(severity: Severity, tag: String?, message: String?, stackTrace: String?): String {
        return "${currentDateTime()} - ${severity.label} - $tag - $message\n$stackTrace"
    }

    /**
     * @param t The [Throwable] from which to extract the stack trace.
     * @return The stack trace as a [String].
     */
    fun getStackTrace(t: Throwable): String = t.stackTraceToString()

    fun getDetailedLog(message: String): String {
        val currentThread = Thread.currentThread()
        val stackTrace: Array<StackTraceElement?>? = currentThread.stackTrace
        val trace = stackTrace?.findRelevantTrace() ?: return message

        val sb = StringBuilder()

        sb.append(message)
        sb.append("\nDetails: ")
        sb.append("${trace.linkableMethod()} [thread: ${currentThread.name}]")

        return sb.toString()
    }

    private fun currentDateTime(): String {
        val instantNow = Clock.System.now()
        return instantNow.toString()
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

    fun StackTraceElement?.linkableLine(): String {
        if (this == null) return "trace is null"
        return "($fileName:$lineNumber)"
    }
}
