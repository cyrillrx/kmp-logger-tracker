package com.cyrillrx.tracker

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplyConsentTest {

    @BeforeTest
    fun init() {
        Tracker.clear()
    }

    @Test
    fun `applyConsent hasConsent and hasTracker should do nothing`() {
        // Given
        val hasConsent = true
        val tracker = InMemoryTracker()
        Tracker.addChild(tracker)
        assertEquals(expected = 1, actual = Tracker.getTrackerCount())

        // When
        Tracker.applyConsent(hasConsent, tracker)

        // Then
        assertEquals(expected = 1, actual = Tracker.getTrackerCount())
    }

    @Test
    fun `applyConsent hasNoConsent and hasNoTrackers should do nothing`() {
        // Given
        val hasConsent = false
        val tracker = InMemoryTracker()
        assertEquals(expected = 0, actual = Tracker.getTrackerCount())

        // When
        Tracker.applyConsent(hasConsent, tracker)

        // Then
        assertEquals(expected = 0, actual = Tracker.getTrackerCount())
    }

    @Test
    fun `applyConsent hasConsent and hasNoTracker should add the tracker`() {
        // Given
        val hasConsent = true
        val tracker = InMemoryTracker()
        assertEquals(expected = 0, actual = Tracker.getTrackerCount())

        // When
        Tracker.applyConsent(hasConsent, tracker)

        // Then
        assertEquals(expected = 1, actual = Tracker.getTrackerCount())
    }

    @Test
    fun `applyConsent hasNoConsent and hasTracker should remove the tracker`() {
        // Given
        val hasConsent = false
        val tracker = InMemoryTracker()
        Tracker.addChild(tracker)
        assertEquals(expected = 1, actual = Tracker.getTrackerCount())

        // When
        Tracker.applyConsent(hasConsent = hasConsent, tracker)

        // Then
        assertEquals(expected = 0, actual = Tracker.getTrackerCount())
    }

    @Test
    fun `applyConsent hasConsent then hasNoConsent should remove`() {
        // Given
        val tracker = InMemoryTracker()
        assertEquals(expected = 0, actual = Tracker.getTrackerCount())

        // When
        Tracker.applyConsent(hasConsent = true, tracker)
        assertEquals(expected = 1, actual = Tracker.getTrackerCount())
        Tracker.applyConsent(hasConsent = false, tracker)

        // Then
        assertEquals(expected = 0, actual = Tracker.getTrackerCount())
    }

    @Test
    fun `applyConsent hasNoConsent then hasConsent should add`() {
        // Given
        val tracker = InMemoryTracker()
        assertEquals(expected = 0, actual = Tracker.getTrackerCount())

        // When
        Tracker.applyConsent(hasConsent = false, tracker)
        assertEquals(expected = 0, actual = Tracker.getTrackerCount())
        Tracker.applyConsent(hasConsent = true, tracker)

        // Then
        assertEquals(expected = 1, actual = Tracker.getTrackerCount())
    }
}
