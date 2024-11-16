package com.cyrillrx.logger;

import kotlin.experimental.ExperimentalObjCName

/**
 * @author Cyril Leroux
 *         Created on 20/10/2015.
 */
enum class Severity(val level: Int, val label: String) {
    FATAL(0, "FATAL"),
    ERROR(1, "ERROR"),
    WARN(2, "WARN"),
    INFO(3, "INFO"),

    @OptIn(ExperimentalObjCName::class)
    @ObjCName("_DEBUG")
    DEBUG(4, "DEBUG"),
    VERBOSE(5, "VERBOSE");
}
