package com.cyrillrx.tracker

import com.cyrillrx.tracker.event.TrackEvent
import kotlin.test.Test
import kotlin.test.assertEquals

class OnTrackerRemovedTest {

    @Test
    fun `removing a tracker without adding first DOES NOT call onTrackerRemoved`() {
        // Given
        val tracker = CountingTracker()

        // When
        Tracker.removeChild(tracker)

        // Then
        assertEquals(expected = 0, actual = tracker.trackerRemovedCount)
    }

    @Test
    fun `removing a tracker calls onTrackerRemoved`() {
        // Given
        val tracker = CountingTracker()

        // When
        Tracker.addChild(tracker)
        Tracker.removeChild(tracker)

        // Then
        assertEquals(expected = 1, actual = tracker.trackerRemovedCount)
    }

    @Test
    fun `removing a tracker twice calls onTrackerRemoved only once`() {
        // Given
        val tracker = CountingTracker()

        // When
        Tracker.addChild(tracker)
        Tracker.removeChild(tracker)
        Tracker.removeChild(tracker)

        // Then
        assertEquals(expected = 1, actual = tracker.trackerRemovedCount)
    }

    private class CountingTracker : TrackerChild("CountingTracker") {
        var trackerRemovedCount: Int = 0
        override fun doTrack(event: TrackEvent) {}
        override fun onTrackerRemoved() {
            super.onTrackerRemoved()
            trackerRemovedCount++
        }

        fun resetCounts() {
            trackerRemovedCount = 0
        }
    }
}
