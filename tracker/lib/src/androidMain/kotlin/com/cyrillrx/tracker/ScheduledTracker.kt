package com.cyrillrx.tracker

import com.cyrillrx.tracker.consumer.ScheduledConsumer
import com.cyrillrx.tracker.event.TrackEvent
import java.util.ArrayDeque
import java.util.Queue
import java.util.concurrent.TimeUnit

/**
 * @author Cyril Leroux
 * Created on 22/04/16.
 */
abstract class ScheduledTracker @JvmOverloads constructor(
    private val timeDuration: Long,
    private val timeUnit: TimeUnit,
    workerCount: Int = DEFAULT_WORKER_COUNT
) : AsyncTracker<Queue<TrackEvent>>(ArrayDeque()) {

    init {
        start(workerCount)
    }

    override fun createConsumer(): ScheduledConsumer? {
        return object : ScheduledConsumer(pendingEvents, timeUnit, timeDuration) {
            override fun doConsume(events: List<TrackEvent>) {
                consumeEvents(events)
            }
        }
    }

    protected abstract fun consumeEvents(events: Collection<TrackEvent>)
}
