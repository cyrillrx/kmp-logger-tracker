package com.cyrillrx.logger

public object LogHelper {
    internal fun addClickableStackTrace(message: String, throwable: Throwable): String =
        "$message\n${throwable.stackTraceToString()}"

    public fun addLinkToCurrentMethod(message: String): String {
        val linkToCurrentMethod = getLinkToCurrentMethod() ?: return message
        return "$message\n$linkToCurrentMethod"
    }

    internal fun formatLogWithDate(entry: LogEntry, message: String = entry.message): String =
        "${entry.timestamp} - ${entry.severity.name} - ${entry.tag} - $message"
}

internal expect fun getLinkToCurrentMethod(): String?
