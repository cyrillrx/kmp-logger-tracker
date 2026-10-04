package com.cyrillrx.logger

import kotlin.experimental.ExperimentalObjCName
import kotlin.jvm.JvmStatic
import kotlin.native.ObjCName

/**
 * This class wraps instances of the {@link LogChild} interface.
 * It allows to customize the logging conditions.
 *
 * @author Cyril Leroux
 *         Created on 03/09/2012.
 */
@OptIn(ExperimentalObjCName::class)
@ObjCName("KMPLogger")
object Logger {
    private val loggers: MutableSet<LogChild> = HashSet()

    private var catcher: ExceptionCatcher? = null

    fun release() {
        loggers.clear()
    }

    fun setCatcher(catcher: ExceptionCatcher) {
        this.catcher = catcher
    }

    @JvmStatic
    fun addChild(child: LogChild) {
        loggers.add(child)
    }

    fun removeChild(child: LogChild) {
        loggers.remove(child)
    }

    fun log(severity: Severity, tag: String, message: String, throwable: Throwable? = null) {
        for (logger in loggers) {
            try {
                logger.log(severity, tag, message, throwable)
            } catch (t: Throwable) {
                try {
                    catcher?.catchException(t)
                } catch (ignored: Exception) {
                    // Prevent the catcher from throwing an exception
                }
            }
        }
    }

    @JvmStatic
    fun verbose(tag: String, message: String, throwable: Throwable? = null) {
        log(Severity.VERBOSE, tag, message, throwable)
    }

    @JvmStatic
    fun debug(tag: String, message: String, throwable: Throwable? = null) {
        log(Severity.DEBUG, tag, message, throwable)
    }

    @JvmStatic
    fun info(tag: String, message: String, throwable: Throwable? = null) {
        log(Severity.INFO, tag, message, throwable)
    }

    @JvmStatic
    fun warning(tag: String, message: String, throwable: Throwable? = null) {
        log(Severity.WARN, tag, message, throwable)
    }

    @JvmStatic
    fun error(tag: String, message: String, throwable: Throwable? = null) {
        log(Severity.ERROR, tag, message, throwable)
    }
}
