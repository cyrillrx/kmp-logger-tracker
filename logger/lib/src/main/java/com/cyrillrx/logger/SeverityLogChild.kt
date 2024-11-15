package com.cyrillrx.logger

/**
 * A [LogChild] wrapper aware of the log severity level.
 *
 * @author Cyril Leroux
 *         Created on 20/10/2015.
 */
abstract class SeverityLogChild(private val maxSeverity: Int) : LogChild() {

    override fun shouldLog(severity: Int, tag: String, message: String, throwable: Throwable?): Boolean =
        maxSeverity >= severity
}
