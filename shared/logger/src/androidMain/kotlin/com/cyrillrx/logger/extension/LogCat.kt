package com.cyrillrx.logger.extension

import android.util.Log
import com.cyrillrx.logger.LogChild
import com.cyrillrx.logger.LogHelper
import com.cyrillrx.logger.Severity
import com.cyrillrx.logger.SeverityLogChild

/**
 * A ready-to-use severity-aware [LogChild] wrapping [Log] class.
 *
 * @author Cyril Leroux
 *         Created on 18/10/2015.
 */
class LogCat(severity: Severity, private val clickableLogs: Boolean) : SeverityLogChild(severity) {

    override fun doLog(severity: Severity, tag: String, message: String, throwable: Throwable?) {
        val finalMessage = createMessageWithTrace(message, throwable)

        when (severity) {
            Severity.VERBOSE -> Log.println(Log.VERBOSE, tag, finalMessage)
            Severity.DEBUG -> Log.println(Log.DEBUG, tag, finalMessage)
            Severity.INFO -> Log.println(Log.INFO, tag, finalMessage)
            Severity.WARN -> Log.println(Log.WARN, tag, finalMessage)
            Severity.ERROR -> Log.println(Log.ERROR, tag, finalMessage)
            Severity.FATAL -> Log.println(Log.ASSERT, tag, finalMessage)
        }
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
