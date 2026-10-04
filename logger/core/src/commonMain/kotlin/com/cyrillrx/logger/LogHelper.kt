package com.cyrillrx.logger

object LogHelper {
    fun addClickableStackTrace(message: String, throwable: Throwable): String =
        "$message\n${throwable.stackTraceToString()}"

    fun addLinkToCurrentMethod(message: String): String {
        val linkToCurrentMethod = getLinkToCurrentMethod() ?: return message
        return "$message\n$linkToCurrentMethod"
    }

    fun formatLogWithDate(entry: LogEntry, message: String = entry.message): String =
        "${entry.timestamp} - ${entry.severity.name} - ${entry.tag} - $message"
}

expect fun getLinkToCurrentMethod(): String?
