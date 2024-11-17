package com.cyrillrx.logger.extension

import com.cyrillrx.logger.LogChild
import com.cyrillrx.logger.LogHelper
import com.cyrillrx.logger.Severity
import com.cyrillrx.logger.SeverityLogChild
import kotlinx.datetime.Clock

/**
 * A ready-to-use severity-aware [LogChild] wrapping `System.out#println(String)` class.
 *
 * @author Cyril Leroux
 *         Created on 18/10/2015.
 */
class SystemOutLog(maxSeverity: Severity, private val clickableLogs: Boolean = false) : SeverityLogChild(maxSeverity) {

    override fun doLog(severity: Severity, tag: String, message: String, throwable: Throwable?) {
        val enhancedMessage = createMessageWithTrace(message, throwable)

        println("${currentDateTime()} - ${severity.label} - $tag - $enhancedMessage")
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

    companion object {
        private fun currentDateTime(): String = Clock.System.now().toString()
    }
}
