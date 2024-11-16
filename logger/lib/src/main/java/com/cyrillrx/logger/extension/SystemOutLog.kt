package com.cyrillrx.logger.extension

import com.cyrillrx.logger.LogChild
import com.cyrillrx.logger.LogHelper
import com.cyrillrx.logger.Severity
import com.cyrillrx.logger.SeverityLogChild

/**
 * A ready-to-use severity-aware [LogChild] wrapping `System.out#println(String)` class.
 *
 * @author Cyril Leroux
 *         Created on 18/10/2015.
 */
class SystemOutLog(maxSeverity: Severity, private val detailedLogs: Boolean = false) : SeverityLogChild(maxSeverity) {

    override fun doLog(severity: Severity, tag: String, message: String, throwable: Throwable?) {

        val finalMessage = if (detailedLogs) LogHelper.getDetailedLog(message) else message

        if (throwable == null) {
            simpleLog(severity, tag, finalMessage)
            return
        }

        val stackTrace = LogHelper.getStackTrace(throwable)

        logWithStackTrace(severity, tag, finalMessage, stackTrace)
    }

    private fun simpleLog(severity: Severity, tag: String, message: String) {
        println(LogHelper.simpleLog(severity, tag, message))
    }

    private fun logWithStackTrace(severity: Severity, tag: String, message: String, stackTrace: String) {
        println(LogHelper.logWithStackTrace(severity, tag, message, stackTrace))
    }

    /** Prints a String into the "standard" output stream and then terminate the line. */
    private fun println(message: String) {
        kotlin.io.println(message)
    }
}
