package com.cyrillrx.logger

/**
 * @author Cyril Leroux
 *         Created on 18/10/2015.
 */
abstract class LogChild {

    /**
     * @param severity The severity level. See {@link Severity}
     * @param tag Used to identify the source of a log message. It usually identifies the class where the log call occurs.
     * @param message The message you would like to log.
     * @param throwable An optional exception linked to the log message.
     */
    fun log(severity: Severity, tag: String, message: String, throwable: Throwable? = null) {
        if (shouldLog(severity, tag, message, throwable)) {
            doLog(severity, tag, message, throwable)
        }
    }

    protected open fun shouldLog(severity: Severity, tag: String, message: String, throwable: Throwable? = null) = true

    protected abstract fun doLog(severity: Severity, tag: String, message: String, throwable: Throwable? = null)
}
