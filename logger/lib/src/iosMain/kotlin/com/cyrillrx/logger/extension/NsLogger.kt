package com.cyrillrx.logger.extension

import com.cyrillrx.logger.LogHelper
import com.cyrillrx.logger.Severity
import com.cyrillrx.logger.SeverityLogChild
import kotlinx.datetime.Clock
import platform.Foundation.NSLog

class NsLogger(severity: Severity) : SeverityLogChild(severity) {

    override fun doLog(severity: Severity, tag: String, message: String, throwable: Throwable?) {
        val enhancedMessage = createMessageWithTrace(message, throwable)

        NSLog("${currentDateTime()} - ${severity.label} - $tag - $enhancedMessage")
    }

    companion object {
        private fun createMessageWithTrace(message: String, throwable: Throwable?): String {
            return if (throwable == null) {
                message
            } else {
                LogHelper.addClickableStackTrace(message, throwable)
            }
        }

        private fun currentDateTime(): String = Clock.System.now().toString()
    }
}
