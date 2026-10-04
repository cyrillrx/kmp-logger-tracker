package com.cyrillrx.logger

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Instant

class LogHelperTest {
    @Test
    fun `formats an entry with its timestamp severity and tag`() {
        val entry = LogEntry(
            severity = Severity.WARN,
            tag = "Sync",
            message = "retrying",
            timestamp = Instant.parse("2026-10-04T10:00:00Z"),
        )

        assertEquals("2026-10-04T10:00:00Z - WARN - Sync - retrying", LogHelper.formatLogWithDate(entry))
    }
}
