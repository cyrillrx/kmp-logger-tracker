package com.cyrillrx.logger

interface Logger {
    fun isLoggable(severity: Severity, tag: String): Boolean

    fun log(
        severity: Severity,
        tag: String,
        throwable: Throwable? = null,
        attributes: Map<String, String> = emptyMap(),
        message: () -> String,
    )
}

fun Logger.verbose(
    tag: String,
    throwable: Throwable? = null,
    attributes: Map<String, String> = emptyMap(),
    message: () -> String,
) = log(Severity.VERBOSE, tag, throwable, attributes, message)

fun Logger.debug(
    tag: String,
    throwable: Throwable? = null,
    attributes: Map<String, String> = emptyMap(),
    message: () -> String,
) = log(Severity.DEBUG, tag, throwable, attributes, message)

fun Logger.info(
    tag: String,
    throwable: Throwable? = null,
    attributes: Map<String, String> = emptyMap(),
    message: () -> String,
) = log(Severity.INFO, tag, throwable, attributes, message)

fun Logger.warn(
    tag: String,
    throwable: Throwable? = null,
    attributes: Map<String, String> = emptyMap(),
    message: () -> String,
) = log(Severity.WARN, tag, throwable, attributes, message)

fun Logger.error(
    tag: String,
    throwable: Throwable? = null,
    attributes: Map<String, String> = emptyMap(),
    message: () -> String,
) = log(Severity.ERROR, tag, throwable, attributes, message)

fun Logger.fatal(
    tag: String,
    throwable: Throwable? = null,
    attributes: Map<String, String> = emptyMap(),
    message: () -> String,
) = log(Severity.FATAL, tag, throwable, attributes, message)
