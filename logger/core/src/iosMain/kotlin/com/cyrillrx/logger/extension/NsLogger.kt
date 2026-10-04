package com.cyrillrx.logger.extension

import com.cyrillrx.logger.LogEntry
import com.cyrillrx.logger.LogHelper
import com.cyrillrx.logger.Severity
import com.cyrillrx.logger.SeverityLogChild
import platform.Foundation.NSLog

class NsLogger(severity: Severity) : SeverityLogChild(severity) {

    override fun log(entry: LogEntry) {
        val enhancedMessage = createMessageWithTrace(entry.message, entry.throwable)

        NSLog(LogHelper.formatLogWithDate(entry, enhancedMessage))
    }

    companion object {
        private fun createMessageWithTrace(message: String, throwable: Throwable?): String {
            return if (throwable == null) {
                message
            } else {
                LogHelper.addClickableStackTrace(message, throwable)
            }
        }
    }
}
