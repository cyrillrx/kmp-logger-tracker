package com.cyrillrx.tracker.event

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.fail

/**
 * @author Cyril Leroux
 *      Created on 25/04/2016.
 */
class TrackEventBuilderTest {
    @Test
    fun `build event with no parameters should fail`() {
        assertFails { TrackEvent.Builder().build() }
    }

    @Test
    fun `Event name is consistent`() {
        val event: TrackEvent = TrackEvent.Builder()
            .setName("Event name")
            .build()

        assertEquals(expected = "Event name", actual = event.name, message = "Event name is inconsistent.")
    }

    @Test
    fun `After addAttribute can retrieve one attribute`() {
        val event: TrackEvent = TrackEvent.Builder()
            .setName("Event name")
            .addAttribute("string", "string_value")
            .build()

        assertEquals(
            expected = "string_value",
            actual = event.getAttribute("string") ?: fail("String attribute is missing."),
            message = "String attribute is inconsistent."
        )
    }

    @Test
    fun `After addAttributes can retrieve attributes`() {
        val event: TrackEvent = TrackEvent.Builder()
            .setName("Event name")
            .addAttributes(mapOf("string" to "string_value"))
            .build()

        assertEquals(
            expected = "string_value",
            actual = event.getAttribute("string") ?: fail("String attribute is missing."),
            message = "String attribute is inconsistent."
        )
    }

    @Test
    fun `After addAttributes with block can retrieve attributes`() {
        val event: TrackEvent = TrackEvent.Builder()
            .setName("Event name")
            .addAttributes {
                put("string", "string_value")
            }
            .build()

        assertEquals(
            expected = "string_value",
            actual = event.getAttribute("string") ?: fail("String attribute is missing."),
            message = "String attribute is inconsistent."
        )
    }
}
