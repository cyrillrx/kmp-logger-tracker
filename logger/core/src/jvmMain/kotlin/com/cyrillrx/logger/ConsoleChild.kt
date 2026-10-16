package com.cyrillrx.logger

/**
 * Prints each entry, prefixed with its timestamp, severity and tag: on the standard error from [Severity.ERROR] up, on
 * the standard output below.
 *
 * Attributes follow the message, and an entry with a throwable is followed by its stack trace.
 *
 * @param clickableLogs appends a link to the method that logged, which IDE consoles render as clickable. An entry with
 * a throwable gets its stack trace instead, whose frames are already clickable.
 */
class ConsoleChild(
    minSeverity: Severity = Severity.VERBOSE,
    private val clickableLogs: Boolean = false,
) : SeverityLogChild(minSeverity) {
    override fun log(entry: LogEntry) {
        val line = LogHelper.formatLogWithDate(entry, buildMessage(entry))
        if (entry.severity >= Severity.ERROR) System.err.println(line) else println(line)
    }

    private fun buildMessage(entry: LogEntry): String {
        val message = appendAttributes(entry)
        val throwable = entry.throwable
        return when {
            throwable != null -> LogHelper.addClickableStackTrace(message, throwable)
            clickableLogs -> LogHelper.addLinkToCurrentMethod(message)
            else -> message
        }
    }

    private fun appendAttributes(entry: LogEntry): String =
        if (entry.attributes.isEmpty()) entry.message else "${entry.message} ${entry.attributes}"
}
