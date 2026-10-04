package com.cyrillrx.logger.extension

import com.cyrillrx.logger.LogChild
import com.cyrillrx.logger.LogEntry
import com.cyrillrx.logger.LogHelper
import com.cyrillrx.logger.Severity
import com.cyrillrx.logger.SeverityLogChild

/**
 * A ready-to-use severity-aware [LogChild] wrapping `System.out#println(String)` class.
 *
 * @author Cyril Leroux
 *         Created on 18/10/2015.
 */
class SystemOutLog(minSeverity: Severity, private val clickableLogs: Boolean = false) : SeverityLogChild(minSeverity) {

    override fun log(entry: LogEntry) {
        val enhancedMessage = createMessageWithTrace(entry.message, entry.throwable)
        val finalMessage = LogHelper.formatLogWithDate(entry, enhancedMessage).let(::println)
        println(finalMessage)
    }

    private fun createMessageWithTrace(message: String, throwable: Throwable?): String {
        return if (throwable != null) {
            LogHelper.addClickableStackTrace(message, throwable)
        } else if (clickableLogs) {
            LogHelper.addLinkToCurrentMethod(message)
        } else {
            message
        }
    }
}
