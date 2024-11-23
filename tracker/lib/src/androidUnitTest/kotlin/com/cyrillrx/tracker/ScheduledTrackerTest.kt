package com.cyrillrx.tracker

import com.cyrillrx.tracker.event.TrackEvent
import com.cyrillrx.tracker.utils.Utils
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * @author Cyril Leroux
 * Created on 22/04/16.
 */
class ScheduledTrackerTest {
    @Test
    fun testConsistency() {
        println("Test started")

        val tracker = DummyScheduledTracker(INTERVAL, UNIT)

        // Wait until the tracker actually starts tracking
        TestUtils.wait100Millis()

        assertTrue(tracker.isEmpty(), message = "Should be empty")

        // Track one event
        tracker.track(TrackEvent(TestUtils.EVENT_NAME))

        // Assert emptiness
        assertTrue(tracker.isEmpty(), message = "Should be empty")

        // Wait enough time for the events to be consumed
        Utils.wait(INTERVAL, UNIT)

        // Assert that the event was sent
        assertEquals(1, tracker.getEventCount(), message = "Should be 1")

        val trackedEvents: List<TrackEvent> = tracker.trackedEvents
        for (event in trackedEvents) {
            assertEquals(TestUtils.EVENT_NAME, event.name)
        }
    }

    @Test
    fun testOneByOne() {
        val tracker = DummyScheduledTracker(INTERVAL, UNIT)

        // Wait until the tracker actually starts tracking
        TestUtils.wait100Millis()

        assertTrue(tracker.isEmpty(), message = "Should be empty")

        // Track EVENT_COUNT events
        TestUtils.trackEventsOneByOne(tracker, EVENT_COUNT, TestUtils.EVENT_NAME)

        // Assert emptiness
        assertTrue(tracker.isEmpty(), message = "Should be empty")

        // Wait enough time for the events to be consumed
        Utils.wait(INTERVAL, UNIT)

        // Assert that the events were sent
        assertEquals(EVENT_COUNT, tracker.getEventCount(), message = "Should be $EVENT_COUNT")
    }

    companion object {
        private const val EVENT_COUNT = 10
        private const val INTERVAL = 200L
        private val UNIT: java.util.concurrent.TimeUnit = java.util.concurrent.TimeUnit.MILLISECONDS
    }
}
