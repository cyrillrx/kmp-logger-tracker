package com.cyrillrx.logger

/**
 * Writes each entry to the platform console: the JVM standard output, and the standard error from [Severity.ERROR] up.
 *
 * Attributes follow the message, and an entry with a throwable is followed by its stack trace.
 *
 * @param clickableLogs appends a link to the method that logged, which IDE consoles render as clickable. An entry with
 * a throwable gets its stack trace instead, whose frames are already clickable. It has no effect on iOS.
 */
class ConsoleChild internal constructor(
    minSeverity: Severity,
    private val clickableLogs: Boolean,
    private val write: (LogEntry, String) -> Unit,
) : SeverityLogChild(minSeverity) {
    constructor(
        minSeverity: Severity = Severity.VERBOSE,
        clickableLogs: Boolean = false,
    ) : this(minSeverity, clickableLogs, ::writeToConsole)

    override fun log(entry: LogEntry) {
        write(entry, buildBody(entry))
    }

    private fun buildBody(entry: LogEntry): String {
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

/** Writes a console body to the platform's native output, which supplies the header (ADR-005). */
internal expect fun writeToConsole(entry: LogEntry, body: String)
