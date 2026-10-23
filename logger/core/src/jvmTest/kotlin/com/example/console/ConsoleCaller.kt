package com.example.console

import com.cyrillrx.logger.CompositeLogger
import com.cyrillrx.logger.ConsoleChild
import com.cyrillrx.logger.warn

// Lives outside the library package, which the stack walk skips, so a link can point at it.
class ConsoleCaller {
    val childErrors = mutableListOf<Throwable>()
    private val logger = CompositeLogger(
        listOf(ConsoleChild(clickableLogs = true)),
        onChildError = { _, error -> childErrors += error },
    )

    fun logWarning() {
        logger.warn("Sync") { "retrying" }
    }
}
