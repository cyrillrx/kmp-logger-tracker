package com.cyrillrx.tracker

import com.cyrillrx.tracker.consumer.EventConsumer
import com.cyrillrx.tracker.consumer.NamedThreadFactory
import com.cyrillrx.tracker.event.TrackEvent
import java.util.Queue
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * @author Cyril Leroux
 * Created on 19/04/16.
 */
abstract class AsyncTracker<EventQueue : Queue<TrackEvent>>(
    protected var pendingEvents: EventQueue
) : TrackerChild("AsyncTracker") {

    override fun doTrack(event: TrackEvent) {
        pendingEvents.add(event)
    }

    /** Starts the pending events consumer(s).  */
    protected fun start(workerCount: Int = DEFAULT_WORKER_COUNT) {
        val processorCount = Runtime.getRuntime().availableProcessors()
        val executorService = Executors.newFixedThreadPool(
            processorCount,
            NamedThreadFactory(THREAD_PREFIX)
        )
        try {
            for (i in 0 until workerCount) {
                submitToService(executorService)
            }
        } finally {
            executorService.shutdown()
        }
    }

    protected open fun submitToService(service: ExecutorService) {
        service.submit(createConsumer())
    }

    protected abstract fun createConsumer(): EventConsumer<EventQueue>?

    companion object {
        private val TAG: String = AsyncTracker::class.java.simpleName

        private val THREAD_PREFIX: String = TAG + "_"
        const val DEFAULT_WORKER_COUNT: Int = 1
    }
}
