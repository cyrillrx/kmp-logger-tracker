package com.cyrillrx.tracker;

import com.cyrillrx.tracker.event.TrackEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * A basic tracker the adds the tracked event to a list.
 *
 * @author Cyril Leroux
 *         Created on 22/04/2016.
 */
class DummyScheduledTracker extends ScheduledTracker {

    public final List<TrackEvent> trackedEvents = new ArrayList<>();

    public DummyScheduledTracker(long timeDuration, TimeUnit timeUnit, int workerCount) {
        super(timeDuration, timeUnit, workerCount);
    }

    public DummyScheduledTracker(long timeDuration, TimeUnit timeUnit) {
        super(timeDuration, timeUnit);
    }

    @Override
    protected void consumeEvents(Collection<TrackEvent> events) {
        trackedEvents.addAll(events);
    }

    boolean isEmpty() { return trackedEvents.isEmpty(); }

    int getEventCount() { return trackedEvents.size(); }
}
