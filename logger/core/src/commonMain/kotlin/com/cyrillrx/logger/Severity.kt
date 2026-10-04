package com.cyrillrx.logger

import kotlin.experimental.ExperimentalObjCName
import kotlin.native.ObjCName

enum class Severity {
    VERBOSE,

    @OptIn(ExperimentalObjCName::class)
    @ObjCName("_DEBUG")
    DEBUG,
    INFO,
    WARN,
    ERROR,
    FATAL,
}
