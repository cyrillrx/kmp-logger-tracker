package com.cyrillrx.tracker

import com.cyrillrx.tracker.event.TrackEvent
import kotlin.test.Test
import kotlin.test.assertEquals

class TrackerChildTest {

    @Test
    fun `track event when shouldTrack returns true`() {
        // Given
        val tracker = InMemoryTracker("in_memory") { event -> event.name == "valid_event" }
        val event = TrackEvent("valid_event")

        // When
        tracker.track(event)

        // Then
        assertEquals(expected = 1, actual = tracker.getEvents().count())
    }

    @Test
    fun `DO NOT track when shouldTrack returns false`() {
        // Given
        val tracker = InMemoryTracker("in_memory") { event -> event.name == "valid_event" }
        val event = TrackEvent(name = "ignored_event")

        // When
        tracker.track(event)

        // Then
        assertEquals(expected = 0, actual = tracker.getEvents().count())
    }
}
