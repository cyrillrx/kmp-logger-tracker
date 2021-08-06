package com.cyrillrx.logger

/**
 * @author Cyril Leroux
 *         Created on 18/10/2015.
 */
abstract class LogChild {

    /**
     * @param severity  The severity level. See {@link Severity}
     * @param tag       Used to identify the source of a log message.
     *                  It usually identifies the class or activity where the log call occurs.
     * @param message   The message you would like to log.
     * @param throwable An exception to log. Might be null.
     */
    fun log(severity: Int, tag: String, message: String, throwable: Throwable? = null) {
        if (shouldLog(severity, tag, message, throwable)) {
            doLog(severity, tag, message, throwable)
        }
    }

    /**
     * @param severity The severity level. See {@link Severity}
     * @param tag      Used to identify the source of a log message.
     *                 It usually identifies the class or activity where the log call occurs.
     * @param message  The message you would like to log.
     */
    fun log(severity: Int, tag: String, message: String) {
        if (shouldLog(severity, tag, message)) {
            doLog(severity, tag, message)
        }
    }

    protected open fun shouldLog(severity: Int, tag: String, message: String, throwable: Throwable? = null): Boolean = true

    /**
     * @param severity  The severity level. See {@link Severity}
     * @param tag       Used to identify the source of a log message.
     *                  It usually identifies the class or activity where the log call occurs.
     * @param message   The message you would like to log.
     * @param throwable An exception to log. Might be null.
     */
    protected abstract fun doLog(severity: Int, tag: String, message: String, throwable: Throwable? = null)
}
