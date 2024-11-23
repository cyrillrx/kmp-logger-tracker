package com.cyrillrx.tracker

import com.cyrillrx.tracker.event.TrackEvent
import java.util.ArrayDeque
import java.util.Queue
import java.util.concurrent.TimeUnit

/**
 * A basic tracker the adds the tracked event to a list.
 *
 * @author Cyril Leroux
 * Created on 22/04/2016.
 */
internal open class DummyStreamingTracker(
    capacity: Int,
    workerCount: Int = DEFAULT_WORKER_COUNT,
    retryQueue: Queue<TrackEvent> = ArrayDeque(),
    retryUnit: TimeUnit = TimeUnit.MILLISECONDS,
    retryInterval: Long = 1000L,
) : StreamingTracker(capacity, workerCount, retryQueue, retryUnit, retryInterval) {
    val trackedEvents: MutableList<TrackEvent> = ArrayList()

    override fun consumeEvent(event: TrackEvent) {
        trackedEvents.add(event)
    }

    fun isEmpty(): Boolean = trackedEvents.isEmpty()

    fun getEventCount(): Int = trackedEvents.size
}
