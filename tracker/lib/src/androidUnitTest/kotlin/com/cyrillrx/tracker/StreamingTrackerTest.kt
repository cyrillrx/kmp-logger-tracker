package com.cyrillrx.tracker

import com.cyrillrx.logger.Logger
import com.cyrillrx.logger.Severity
import com.cyrillrx.logger.extension.SystemOutLog
import com.cyrillrx.tracker.event.TrackEvent
import org.junit.BeforeClass
import java.util.ArrayDeque
import java.util.concurrent.TimeUnit
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * @author Cyril Leroux
 * Created on 20/04/16.
 */
class StreamingTrackerTest {
    @Test
    fun testConsistency() {
        Logger.info(TAG, "Started testConsistency", null)

        val tracker = DummyStreamingTracker(10)

        assertTrue(tracker.isEmpty(), message = "Should be empty")

        // Track one event
        tracker.track(TrackEvent("Event name"))
        TestUtils.wait100Millis()

        assertEquals(
            1, tracker.getEventCount(), message = "Should be 1"
        )

        val trackedEvents: List<TrackEvent> = tracker.trackedEvents
        for (event in trackedEvents) {
            assertEquals("Event name", event.name)
        }
    }

    @Test
    fun testRetry() {
        val tracker =
            RetryTracker(10, 1, ArrayDeque(), TimeUnit.MILLISECONDS, 200)

        assertTrue(tracker.isEmpty(), message = "Should be empty")

        // First track should fail
        tracker.track(TrackEvent(TestUtils.EVENT_NAME))

        TestUtils.wait100Millis()
        assertTrue(tracker.isEmpty(), message = "Should be empty")

        // Wait for retry to be performed
        TestUtils.wait100Millis()
        TestUtils.wait100Millis()
        assertEquals(1, tracker.getEventCount(), message = "Should be 1")
    }

    @Test
    fun testOneByOne() {
        val tracker = DummyStreamingTracker(100)

        assertTrue(tracker.isEmpty(), message = "Should be empty")

        // Track EVENT_COUNT events
        TestUtils.trackEventsOneByOne(tracker, EVENT_COUNT, TestUtils.EVENT_NAME)
        TestUtils.wait100Millis()
        assertEquals(EVENT_COUNT, tracker.getEventCount(), message = "Should be $EVENT_COUNT")

        // Track EVENT_COUNT events
        TestUtils.trackEventsOneByOne(tracker, EVENT_COUNT, TestUtils.EVENT_NAME)
        TestUtils.wait100Millis()
        val totalEventCount = EVENT_COUNT * 2
        assertEquals(totalEventCount, tracker.getEventCount(), message = "Should be $totalEventCount")
    }

    companion object {
        private val TAG: String = StreamingTracker::class.java.simpleName

        private const val EVENT_COUNT = 10

        @JvmStatic
        @BeforeClass
        fun initLogger() {
            Logger.addChild(SystemOutLog(Severity.VERBOSE, false))
        }
    }
}
