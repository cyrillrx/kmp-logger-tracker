package com.cyrillrx.logger

/**
 * @author Cyril Leroux
 *      Created on 03/01/2019.
 */
object LogHelper {
    /**
     * @param t The [Throwable] from which to extract the stack trace.
     * @return The stack trace as a [String].
     */
    fun getStackTrace(t: Throwable): String = t.stackTraceToString()


    fun addClickableStackTrace(message: String, throwable: Throwable): String {
        return "$message\n${throwable.stackTraceToString()}"
    }

    fun addLinkToCurrentMethod(message: String): String {
        val linkToCurrentMethod = getLinkToCurrentMethod() ?: return message
        return "$message\n$linkToCurrentMethod"
    }
}

expect fun getLinkToCurrentMethod(): String?
