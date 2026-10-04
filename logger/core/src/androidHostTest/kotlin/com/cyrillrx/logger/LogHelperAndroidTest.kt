package com.cyrillrx.logger

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class LogHelperAndroidTest {
    @Test
    fun `links to the caller of a built-in child`() {
        val stack = stackOf(
            "java.lang.Thread",
            "com.cyrillrx.logger.LogHelper",
            "com.cyrillrx.logger.extension.LogCat",
            "com.cyrillrx.logger.CompositeLogger",
            "com.cyrillrx.logger.LoggerKt",
            CALLER,
        )

        assertEquals(CALLER, stack.findRelevantTrace()?.className)
    }

    @Test
    fun `links to the caller of a child declared outside the library`() {
        val stack = stackOf(
            "java.lang.Thread",
            "com.cyrillrx.logger.LogHelper",
            "com.example.app.MyLogChild",
            "com.cyrillrx.logger.CompositeLogger",
            "com.cyrillrx.logger.LoggerKt",
            CALLER,
        )

        assertEquals(CALLER, stack.findRelevantTrace()?.className)
    }

    @Test
    fun `links to the caller of a logger wrapped outside the library`() {
        val stack = stackOf(
            "java.lang.Thread",
            "com.cyrillrx.logger.LogHelper",
            "com.cyrillrx.logger.extension.LogCat",
            "com.cyrillrx.logger.CompositeLogger",
            "com.example.app.PrefixedLogger",
            "com.cyrillrx.logger.LoggerKt",
            CALLER,
        )

        assertEquals(CALLER, stack.findRelevantTrace()?.className)
    }

    @Test
    fun `links to the caller of a nested log call made by a child`() {
        val stack = stackOf(
            "java.lang.Thread",
            "com.cyrillrx.logger.LogHelper",
            "com.cyrillrx.logger.extension.LogCat",
            "com.cyrillrx.logger.CompositeLogger",
            "com.cyrillrx.logger.LoggerKt",
            "com.example.app.MyLogChild",
            "com.cyrillrx.logger.CompositeLogger",
            "com.cyrillrx.logger.LoggerKt",
            CALLER,
        )

        assertEquals("com.example.app.MyLogChild", stack.findRelevantTrace()?.className)
    }

    @Test
    fun `links to the child error handler that logs through the Log facade`() {
        val stack = stackOf(
            "java.lang.Thread",
            "com.cyrillrx.logger.LogHelper",
            "com.cyrillrx.logger.extension.LogCat",
            "com.cyrillrx.logger.CompositeLogger",
            "com.cyrillrx.logger.Log",
            "com.cyrillrx.logger.LoggerKt",
            "com.example.app.ChildErrorHandler",
            "com.cyrillrx.logger.CompositeLogger",
            "com.cyrillrx.logger.Log",
            "com.cyrillrx.logger.LoggerKt",
            CALLER,
        )

        assertEquals("com.example.app.ChildErrorHandler", stack.findRelevantTrace()?.className)
    }

    @Test
    fun `finds no link without a library frame`() {
        val stack = stackOf("java.lang.Thread", CALLER)

        assertNull(stack.findRelevantTrace())
    }

    private fun stackOf(vararg classNames: String): Array<StackTraceElement?> =
        Array(classNames.size) { StackTraceElement(classNames[it], "method", "File.kt", it) }

    private companion object {
        const val CALLER = "com.example.app.SyncRepository"
    }
}
