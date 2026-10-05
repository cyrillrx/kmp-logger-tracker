package com.cyrillrx.logger

import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals

class LogTest {
    @AfterTest
    fun restoreDefaultLogger() {
        Log.install(CompositeLogger())
    }

    @Test
    fun `delegates to the installed logger`() {
        val child = RamLogChild()
        Log.install(CompositeLogger(listOf(child)))

        Log.info(TAG) { "message" }

        assertEquals("message", child.entries().single().message)
    }

    private companion object {
        const val TAG = "LogTest"
    }
}
