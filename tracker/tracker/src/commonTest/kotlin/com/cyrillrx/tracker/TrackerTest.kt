package com.cyrillrx.tracker

import com.cyrillrx.tracker.event.TrackEvent
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class TrackerTest {

    @BeforeTest
    fun init() {
        Tracker.clear()
    }

    @Test
    fun `tracker count is 0 after creation`() {
        val trackerCount = Tracker.getTrackerCount()
        assertEquals(expected = 0, actual = trackerCount)
    }

    @Test
    fun `tracker count is 1 after add`() {
        // When
        val tracker = InMemoryTracker()
        Tracker.addChild(tracker)

        // Then
        val trackerCount = Tracker.getTrackerCount()
        assertEquals(expected = 1, actual = trackerCount)
    }

    @Test
    fun `tracker count is 0 after add and remove`() {
        // When
        val tracker = InMemoryTracker()
        Tracker.addChild(tracker)
        Tracker.removeChild(tracker)

        // Then
        val trackerCount = Tracker.getTrackerCount()
        assertEquals(expected = 0, actual = trackerCount)
    }

    @Test
    fun `tracker count is 0 with only a remove`() {
        // When
        val tracker = InMemoryTracker()
        Tracker.removeChild(tracker)

        // Then
        val trackerCount = Tracker.getTrackerCount()
        assertEquals(expected = 0, actual = trackerCount)
    }

    @Test
    fun `Tracking an event with initializing context should not crash`() {
        // Given
        val tracker = InMemoryTracker()
        Tracker.addChild(tracker)

        val inputEvent = TrackEvent(name = "test_name")

        // When
//        Tracker.setupApp(TrackingApp("test_app", "test_version"))

        Tracker.track(inputEvent)
    }

    @Test
    fun `track should call trackers once`() {
        // Given
        val tracker1 = InMemoryTracker("tracker1")
        Tracker.addChild(tracker1)

        val tracker2 = InMemoryTracker("tracker2")
        Tracker.addChild(tracker2)

        val inputEvent = TrackEvent(name = "test_name")

        // When
        Tracker.track(inputEvent)

        // Then
        assertEquals(expected = 1, actual = tracker1.getEvents().size)
        assertEquals(expected = 1, actual = tracker2.getEvents().size)
    }

    @Test
    fun `track should forward values`() {
        // Given
        val tracker = InMemoryTracker()
        Tracker.addChild(tracker)

        val inputEvent = TrackEvent(
            name = "test_name",
            attributes = hashMapOf("key" to "value"),
        )

        // When
        Tracker.track(inputEvent)

        // Then
        val event = tracker.getEvents().first()
        assertEquals(expected = "test_name", actual = event.name)
        assertEquals(expected = "value", actual = event.attributes["key"])
    }

    @Test
    fun `track after add and remove should do nothing`() {
        // Given
        val tracker = InMemoryTracker()
        Tracker.addChild(tracker)
        Tracker.removeChild(tracker.name)

        val inputEvent = TrackEvent(name = "test_name")

        // When
        Tracker.track(inputEvent)

        // Then
        assertEquals(expected = 0, actual = tracker.getEvents().size)
    }
}
