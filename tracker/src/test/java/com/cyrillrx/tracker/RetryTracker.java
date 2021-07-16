package com.cyrillrx.tracker;

import com.cyrillrx.tracker.event.TrackEvent;

import java.util.Queue;
import java.util.concurrent.TimeUnit;

/**
 * A tracker that fails to track the first event on purpose.
 *
 * @author Cyril Leroux
 *         Created on 26/04/2016.
 */
class RetryTracker extends DummyStreamingTracker {

    private boolean fail = true;

    public RetryTracker(int capacity, int workerCount) { super(capacity, workerCount); }

    public RetryTracker(int capacity) { super(capacity); }

    public RetryTracker(int capacity, int workerCount, Queue<TrackEvent> queue, TimeUnit unit, long interval) {
        super(capacity, workerCount, queue, unit, interval);
    }

    @Override
    protected void consumeEvent(TrackEvent event) {
        if (fail) {
            fail = false;
            throw new RuntimeException("Failed tracking the event on purpose !");

        } else {
            super.consumeEvent(event);
        }
    }
}
