package com.cyrillrx.tracker.consumer

import com.cyrillrx.logger.Logger.info
import com.cyrillrx.tracker.event.TrackEvent
import java.util.Queue
import java.util.concurrent.BlockingQueue

/**
 * @author Cyril Leroux
 * Created on 20/04/16.
 */
abstract class StreamingConsumer(
    queue: BlockingQueue<TrackEvent>,
    private var retryQueue: Queue<TrackEvent>,
) : EventConsumer<BlockingQueue<TrackEvent>>(queue) {

    override fun consume() {
        try {
            val event: TrackEvent = events.take()

            if (event is StopEvent) {
                running = false
            }

            try {
                doConsume(event)
            } catch (e: Exception) {
                info(TAG, "Error while consuming. Adding event to the retry queue.", e)
                retryQueue.add(event)
            }
        } catch (e: InterruptedException) {
            running = false
        }
    }

    protected abstract fun doConsume(event: TrackEvent)

    companion object {
        private val TAG: String = StreamingConsumer::class.java.getSimpleName()
    }
}
