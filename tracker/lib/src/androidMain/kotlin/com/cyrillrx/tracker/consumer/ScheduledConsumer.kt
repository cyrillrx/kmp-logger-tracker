package com.cyrillrx.tracker.consumer

import com.cyrillrx.tracker.event.TrackEvent
import com.cyrillrx.tracker.utils.Utils
import java.util.Queue
import java.util.concurrent.TimeUnit

/**
 * @author Cyril Leroux
 * Created on 21/04/16.
 */
abstract class ScheduledConsumer(
    queue: Queue<TrackEvent>,
    unit: TimeUnit,
    duration: Long
) : EventConsumer<Queue<TrackEvent>>(queue) {

    private val timeUnit: TimeUnit = unit
    private val timeDuration = duration

    override fun consume() {
        // Events to be sent

        val pendingEvents: MutableList<TrackEvent> = ArrayList()

        var event: TrackEvent?
        while (!events.isEmpty()) {
            event = events.poll()

            // If stop received, stop the batch and send pending events
            if (event is StopEvent) {
                running = false
                break
            }
            pendingEvents.add(event)
        }

        try {
            doConsume(pendingEvents)
        } catch (e: java.lang.Exception) {
            // Retry policy
            // TODO log warning

            events.addAll(pendingEvents)
        }

        Utils.wait(timeDuration, timeUnit)
    }

    protected abstract fun doConsume(events: List<TrackEvent>)
}
