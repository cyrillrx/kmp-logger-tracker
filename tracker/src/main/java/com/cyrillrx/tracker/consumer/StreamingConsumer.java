package com.cyrillrx.tracker.consumer;

import com.cyrillrx.logger.Logger;
import com.cyrillrx.tracker.event.TrackEvent;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;

/**
 * @author Cyril Leroux
 *         Created on 20/04/16.
 */
public abstract class StreamingConsumer extends EventConsumer<BlockingQueue<TrackEvent>> {

    private static final String TAG = StreamingConsumer.class.getSimpleName();

    protected Queue<TrackEvent> retryQueue;

    public StreamingConsumer(BlockingQueue<TrackEvent> queue, Queue<TrackEvent> retryQueue) {
        super(queue);
        this.retryQueue = retryQueue;
    }

    public StreamingConsumer(BlockingQueue<TrackEvent> queue) { this(queue, null); }

    @Override
    protected void consume() {

        try {
            final TrackEvent event = events.take();

            if (STOP_EVENT.equals(event)) {
                running = false;
            }

            try {
                doConsume(event);

            } catch (Exception e) {

                if (retryQueue == null) {
                    Logger.error(TAG, "Error while consuming the event without a retry queue. Rethrowing exception", e);
                    throw e;

                } else {
                    Logger.info(TAG, "Error while consuming. Adding event to the retry queue.", e);
                    retryQueue.add(event);
                }
            }

        } catch (InterruptedException e) {
            running = false;
        }
    }

    protected abstract void doConsume(TrackEvent event);
}
