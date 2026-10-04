package com.cyrillrx.logger

import kotlin.test.Test
import kotlin.test.assertEquals

class SeverityTest {
    @Test
    fun `severities are ordered from the least to the most severe`() {
        val expected = listOf(
            Severity.VERBOSE,
            Severity.DEBUG,
            Severity.INFO,
            Severity.WARN,
            Severity.ERROR,
            Severity.FATAL,
        )

        assertEquals(expected, Severity.entries.sorted())
    }
}
