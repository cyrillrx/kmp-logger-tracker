package com.cyrillrx.tracker.event

import com.cyrillrx.tracker.context.Connectivity
import com.cyrillrx.tracker.context.FakeUser
import com.cyrillrx.tracker.context.TrackerContext
import com.cyrillrx.tracker.context.TrackingApp
import com.cyrillrx.tracker.context.createTrackingDevice
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.test.fail

class TrackEventTest {
    @Test
    fun `Event is not valid without a context`() {
        val event = TrackEvent("Event name")
        assertFalse(event.isValid(), message = "Event is valid.")
    }

    @Test
    fun `Event is valid with a context`() {
        val event = TrackEvent("Event name")
        event.context = createFakeContext()
        assertTrue(event.isValid(), message = "Event is not valid.")
    }

    @Test
    fun `After event creation attribute count is zero`() {
        val event = TrackEvent("Event name")
        assertEquals(expected = 0, actual = event.attributes.size, message = "Attributes count is inconsistent.")
    }

    @Test
    fun `After adding one attribute attribute count is one`() {
        val event = TrackEvent("Event name")
        event.addAttribute("string", "string_value")

        assertEquals(expected = 1, actual = event.attributes.size, message = "Attributes count is inconsistent.")
    }

    @Test
    fun `Can retrieve attributes added at event creation`() {
        val event = TrackEvent(name = "Event name", attributes = mutableMapOf("string" to "string_value"))

        assertEquals(
            expected = "string_value",
            actual = event.getAttribute("string") ?: fail("String attribute is missing."),
            message = "String attribute is inconsistent."
        )
    }

    @Test
    fun `After addAttribute can retrieve one attribute`() {
        val event = TrackEvent("Event name")
        event.addAttribute("string", "string_value")

        assertEquals(
            expected = "string_value",
            actual = event.getAttribute("string") ?: fail("String attribute is missing."),
            message = "String attribute is inconsistent."
        )
    }

    @Test
    fun `After addAttribute can retrieve attributes as dictionary`() {
        val event = TrackEvent("Event name")
        event.addAttribute("string", "string_value")

        val dictionary = event.getAttributesAsDictionary()

        assertEquals(
            expected = "string_value",
            actual = dictionary["string"] ?: fail("String attribute is missing."),
            message = "String attribute is inconsistent."
        )
    }

    @Test
    fun `After addAttributes can retrieve attributes`() {
        val event = TrackEvent("Event name")
        event.addAttributes(mapOf("string" to "string_value"))

        assertEquals(
            expected = "string_value",
            actual = event.getAttribute("string") ?: fail("String attribute is missing."),
            message = "String attribute is inconsistent."
        )
    }

    @Test
    fun `Float attributes are consistent`() {
        val event = TrackEvent("Event name")
        event.addAttribute("float", 2f)

        assertEquals(
            expected = 2f,
            actual = event.getAttribute("float") ?: fail("Float attribute is missing."),
            absoluteTolerance = 0.1f,
            message = "Float attribute is inconsistent."
        )
    }

    @Test
    fun `Long attributes are consistent`() {
        val event = TrackEvent("Event name")
        event.addAttribute("long", 1L)

        assertEquals(
            expected = 1L,
            actual = event.getAttribute("long") ?: fail("Long attribute is missing."),
            message = "Long attribute is inconsistent."
        )
    }

    @Test
    fun `Int attributes are consistent`() {
        val event = TrackEvent("Event name")
        event.addAttribute("int", 1)

        assertEquals(
            expected = 1,
            actual = event.getAttribute("int") ?: fail("Int attribute is missing."),
            message = "Int attribute is inconsistent."
        )
    }

    @Test
    fun `Complex attributes are consistent`() {
        val stringMap: MutableMap<String, String> = HashMap()
        stringMap["string_key"] = "string_value"

        val anyMap: MutableMap<String, Any> = HashMap()
        anyMap["any_key"] = StringWrapper("nested_Value")

        val event: TrackEvent = TrackEvent.Builder()
            .setName("Event name")
            .addAttributes(stringMap)
            .addAttributes(anyMap)
            .build()

        assertEquals(expected = "Event name", actual = event.name, message = "Event name is inconsistent.")

        val stringValue: String = event.getAttribute("string_key") ?: fail("Attribute 'string_key' is missing.")
        assertEquals(
            expected = "string_value",
            actual = stringValue,
            message = "Attribute 'string_key' is inconsistent."
        )

        val stringWrapper: StringWrapper = event.getAttribute("any_key") ?: fail("Attribute 'any_key' is missing.")
        assertEquals(
            expected = "nested_Value",
            actual = stringWrapper.string,
            message = "Attribute 'complex_1' is inconsistent."
        )
    }

    private class StringWrapper(val string: String)

    companion object {
        fun createFakeContext(): TrackerContext {
            return TrackerContext(
                app = TrackingApp("AwesomeApp", "2.0"),
                user = FakeUser("john_doe", "John Doe", "john_doe@company.com"),
                device = createTrackingDevice(),
                connectivity = Connectivity.UNKNOWN,
            )
        }
    }
}
