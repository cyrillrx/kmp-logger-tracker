package com.cyrillrx.tracker

import com.cyrillrx.tracker.consumer.ScheduledConsumer
import com.cyrillrx.tracker.consumer.StreamingConsumer
import com.cyrillrx.tracker.event.TrackEvent
import java.util.Queue
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.BlockingQueue
import java.util.concurrent.ExecutorService
import java.util.concurrent.TimeUnit

/**
 * @author Cyril Leroux
 * Created on 20/04/16.
 */
abstract class StreamingTracker(
    capacity: Int,
    workerCount: Int = DEFAULT_WORKER_COUNT,
    private var retryQueue: Queue<TrackEvent>,
    protected var retryUnit: TimeUnit,
    protected var retryInterval: Long,
) : AsyncTracker<BlockingQueue<TrackEvent>>(createQueue(capacity)) {

    init {
        start(workerCount)
    }

    override fun submitToService(service: ExecutorService) {
        super.submitToService(service)

        // Set the retry consumer
        if (retryInterval <= 0L) {
            return
        }

        val scheduledConsumer: ScheduledConsumer = object : ScheduledConsumer(retryQueue, retryUnit, retryInterval) {
            override fun doConsume(events: List<TrackEvent>) {
                pendingEvents.addAll(events)
            }
        }
        service.submit(scheduledConsumer)
    }

    override fun createConsumer(): StreamingConsumer {
        return object : StreamingConsumer(pendingEvents, retryQueue) {
            override fun doConsume(event: TrackEvent) {
                consumeEvent(event)
            }
        }
    }

    protected abstract fun consumeEvent(event: TrackEvent)

    companion object {
        private fun createQueue(capacity: Int): BlockingQueue<TrackEvent> {
            return ArrayBlockingQueue(capacity, true)
        }
    }
}
