package com.cyrillrx.tracker

import com.cyrillrx.tracker.event.TrackEvent

class InMemoryTracker(
    name: String = "in_memory",
    private val shouldTrackerPredicate: (TrackEvent) -> Boolean = { true },
) : TrackerChild(name) {
    private val events = mutableListOf<TrackEvent>()

    override fun shouldTrack(event: TrackEvent): Boolean = shouldTrackerPredicate(event)

    override fun doTrack(event: TrackEvent) {
        events.add(event)
    }

    fun getEvents(): List<TrackEvent> = events
}
