package com.cyrillrx.tracker

import com.cyrillrx.tracker.event.TrackEvent
import java.util.Queue
import java.util.concurrent.TimeUnit

/**
 * A tracker that fails to track the first event on purpose.
 *
 * @author Cyril Leroux
 * Created on 26/04/2016.
 */
internal class RetryTracker(
    capacity: Int,
    workerCount: Int = DEFAULT_WORKER_COUNT,
    retryQueue: Queue<TrackEvent>,
    retryUnit: TimeUnit,
    retryInterval: Long,
) : DummyStreamingTracker(capacity, workerCount, retryQueue, retryUnit, retryInterval) {
    private var fail = true

    protected override fun consumeEvent(event: TrackEvent) {
        if (fail) {
            fail = false
            throw RuntimeException("Failed tracking the event on purpose !")
        } else {
            super.consumeEvent(event)
        }
    }
}
