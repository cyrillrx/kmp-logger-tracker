package com.cyrillrx.tracker

import com.cyrillrx.tracker.event.TrackEvent
import java.util.concurrent.TimeUnit

/**
 * A basic tracker the adds the tracked event to a list.
 *
 * @author Cyril Leroux
 * Created on 22/04/2016.
 */
internal class DummyScheduledTracker(
    timeDuration: Long,
    timeUnit: TimeUnit,
    workerCount: Int = DEFAULT_WORKER_COUNT
) : ScheduledTracker(timeDuration, timeUnit, workerCount) {
    val trackedEvents: MutableList<TrackEvent> = ArrayList()

    protected override fun consumeEvents(events: Collection<TrackEvent>) {
        trackedEvents.addAll(events)
    }

    fun isEmpty(): Boolean = trackedEvents.isEmpty()

    fun getEventCount(): Int = trackedEvents.size
}
