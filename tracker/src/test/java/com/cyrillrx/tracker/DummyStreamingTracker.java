package com.cyrillrx.tracker;

import com.cyrillrx.tracker.event.TrackEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

/**
 * A basic tracker the adds the tracked event to a list.
 *
 * @author Cyril Leroux
 *         Created on 22/04/2016.
 */
class DummyStreamingTracker extends StreamingTracker {

    public final List<TrackEvent> trackedEvents = new ArrayList<>();

    public DummyStreamingTracker(int capacity, int workerCount) {
        super(capacity, workerCount);
    }

    public DummyStreamingTracker(int capacity) {
        super(capacity);
    }

    public DummyStreamingTracker(int capacity, int workerCount, Queue<TrackEvent> queue, TimeUnit unit, long interval) {
        super(capacity, workerCount, queue, unit, interval);
    }

    @Override
    protected void consumeEvent(TrackEvent event) { trackedEvents.add(event); }

    boolean isEmpty() { return trackedEvents.isEmpty(); }

    int getEventCount() { return trackedEvents.size(); }
}
