package com.cyrillrx.tracker;

import com.cyrillrx.tracker.consumer.EventConsumer;
import com.cyrillrx.tracker.consumer.NamedThreadFactory;
import com.cyrillrx.tracker.event.TrackEvent;

import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Cyril Leroux
 *         Created on 19/04/16.
 */
public abstract class AsyncTracker<EventQueue extends Queue<TrackEvent>> extends TrackerChild {

    private static final String TAG = AsyncTracker.class.getSimpleName();

    private static final String THREAD_PREFIX = TAG + "_";
    protected static final int DEFAULT_WORKER_COUNT = 1;

    protected EventQueue pendingEvents;

    public AsyncTracker(EventQueue queue) { pendingEvents = queue; }

    /**
     * Adds the event to the queue where it will be consume.
     *
     * @param event The event to add to the queue.
     */
    @Override
    protected void doTrack(TrackEvent event) { pendingEvents.add(event); }

    /** Starts the pending events consumer(s). */
    protected void start() { start(DEFAULT_WORKER_COUNT); }

    /** Starts the pending events consumer(s). */
    protected void start(int workerCount) {

        final int processorCount = Runtime.getRuntime().availableProcessors();
        final ExecutorService executorService = Executors.newFixedThreadPool(
                processorCount,
                new NamedThreadFactory(THREAD_PREFIX));

        try {
            for (int i = 0; i < workerCount; ++i) {
                submitToService(executorService);
            }

        } finally {
            executorService.shutdown();
        }
    }

    protected void submitToService(ExecutorService service) { service.submit(createConsumer()); }

    protected abstract EventConsumer<EventQueue> createConsumer();
}
