package com.cyrillrx.logger

import kotlin.time.Clock
import kotlin.time.Instant

/**
 * @author Cyril Leroux
 *      Created on 03/01/2019.
 */
object LogHelper {
    fun addClickableStackTrace(message: String, throwable: Throwable): String {
        return "$message\n${throwable.stackTraceToString()}"
    }

    fun addLinkToCurrentMethod(message: String): String {
        val linkToCurrentMethod = getLinkToCurrentMethod() ?: return message
        return "$message\n$linkToCurrentMethod"
    }

    fun formatLogWithDate(
        severity: Severity,
        tag: String,
        message: String,
        instant: Instant = Clock.System.now(),
    ): String = "$instant - ${severity.label} - $tag - $message"
}

expect fun getLinkToCurrentMethod(): String?
