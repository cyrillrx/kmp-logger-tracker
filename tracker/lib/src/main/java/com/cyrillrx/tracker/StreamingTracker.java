package com.cyrillrx.tracker;

import com.cyrillrx.tracker.consumer.ScheduledConsumer;
import com.cyrillrx.tracker.consumer.StreamingConsumer;
import com.cyrillrx.tracker.event.TrackEvent;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Cyril Leroux
 *         Created on 20/04/16.
 */
public abstract class StreamingTracker extends AsyncTracker<BlockingQueue<TrackEvent>> {

    private Queue<TrackEvent> retryQueue;
    protected TimeUnit retryUnit;
    protected long retryInterval;

    public StreamingTracker(int capacity, int workerCount) {
        super(createQueue(capacity));

        start(workerCount);
    }

    public StreamingTracker(int capacity) { this(capacity, DEFAULT_WORKER_COUNT); }

    public StreamingTracker(int capacity, int workerCount, Queue<TrackEvent> queue, TimeUnit unit, long interval) {
        super(createQueue(capacity));

        this.retryQueue = queue;
        this.retryUnit = unit;
        this.retryInterval = interval;

        start(workerCount);
    }

    @Override
    protected void submitToService(ExecutorService service) {
        super.submitToService(service);

        // Set the retry consumer
        if (retryQueue == null || retryUnit == null || retryInterval <= 0L) { return; }

        final ScheduledConsumer scheduledConsumer = new ScheduledConsumer(retryQueue, retryUnit, retryInterval) {
            @Override
            protected void doConsume(List<TrackEvent> events) { pendingEvents.addAll(events); }
        };
        service.submit(scheduledConsumer);
    }

    @Override
    protected StreamingConsumer createConsumer() {
        return new StreamingConsumer(pendingEvents, retryQueue) {
            @Override
            protected void doConsume(TrackEvent event) { consumeEvent(event); }
        };
    }

    protected abstract void consumeEvent(TrackEvent event);

    private static BlockingQueue<TrackEvent> createQueue(int capacity) {
        return new ArrayBlockingQueue<>(capacity, true);
    }
}
