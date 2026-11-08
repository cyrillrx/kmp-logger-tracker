package com.cyrillrx.logger

import kotlin.experimental.ExperimentalObjCName
import kotlin.native.ObjCName

@OptIn(ExperimentalObjCName::class)
@ObjCName("KMPLogger")
public interface Logger {
    public fun isLoggable(severity: Severity, tag: String): Boolean

    public fun log(
        severity: Severity,
        tag: String,
        throwable: Throwable? = null,
        attributes: Map<String, String> = emptyMap(),
        message: () -> String,
    )
}

public fun Logger.verbose(
    tag: String,
    throwable: Throwable? = null,
    attributes: Map<String, String> = emptyMap(),
    message: () -> String,
): Unit = log(Severity.VERBOSE, tag, throwable, attributes, message)

public fun Logger.debug(
    tag: String,
    throwable: Throwable? = null,
    attributes: Map<String, String> = emptyMap(),
    message: () -> String,
): Unit = log(Severity.DEBUG, tag, throwable, attributes, message)

public fun Logger.info(
    tag: String,
    throwable: Throwable? = null,
    attributes: Map<String, String> = emptyMap(),
    message: () -> String,
): Unit = log(Severity.INFO, tag, throwable, attributes, message)

public fun Logger.warn(
    tag: String,
    throwable: Throwable? = null,
    attributes: Map<String, String> = emptyMap(),
    message: () -> String,
): Unit = log(Severity.WARN, tag, throwable, attributes, message)

public fun Logger.error(
    tag: String,
    throwable: Throwable? = null,
    attributes: Map<String, String> = emptyMap(),
    message: () -> String,
): Unit = log(Severity.ERROR, tag, throwable, attributes, message)

public fun Logger.fatal(
    tag: String,
    throwable: Throwable? = null,
    attributes: Map<String, String> = emptyMap(),
    message: () -> String,
): Unit = log(Severity.FATAL, tag, throwable, attributes, message)
