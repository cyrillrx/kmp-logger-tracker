package com.cyrillrx.logger.extension

import com.cyrillrx.logger.LogChild
import com.cyrillrx.logger.LogHelper
import com.cyrillrx.logger.SeverityLogChild

/**
 * A ready-to-use severity-aware [LogChild] wrapping `System.out#println(String)` class.
 *
 * @author Cyril Leroux
 *         Created on 18/10/2015.
 */
class SystemOutLog(severity: Int, private val detailedLogs: Boolean = false) : SeverityLogChild(severity) {

    override fun doLog(severity: Int, tag: String, message: String, throwable: Throwable?) {

        val finalMessage = if (detailedLogs) LogHelper.getDetailedLog(message) else message

        if (throwable == null) {
            simpleLog(severity, tag, finalMessage)
            return
        }

        val stackTrace = LogHelper.getStackTrace(throwable)
        if (stackTrace == null) {
            simpleLog(severity, tag, finalMessage)
            return
        }

        logWithStackTrace(severity, tag, finalMessage, stackTrace)
    }

    private fun simpleLog(severity: Int, tag: String, message: String) {
        println(LogHelper.simpleLog(severity, tag, message))
    }

    private fun logWithStackTrace(severity: Int, tag: String, message: String, stackTrace: String) {
        println(LogHelper.logWithStackTrace(severity, tag, message, stackTrace))
    }

    /**
     * Prints a String into the "standard" output stream and then terminate the line.
     *
     * @param x The `String` to be printed.
     */
    private fun println(x: String) {
        println(x)
    }
}
