package com.cyrillrx.tracker;

import com.cyrillrx.tracker.consumer.ScheduledConsumer;
import com.cyrillrx.tracker.event.TrackEvent;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

/**
 * @author Cyril Leroux
 *         Created on 22/04/16.
 */
public abstract class ScheduledTracker extends AsyncTracker<Queue<TrackEvent>> {

    private final TimeUnit timeUnit;
    private final long timeDuration;

    public ScheduledTracker(long timeDuration, TimeUnit timeUnit, int workerCount) {
        super(new ArrayDeque<>());

        this.timeDuration = timeDuration;
        this.timeUnit = timeUnit;

        start(workerCount);
    }

    public ScheduledTracker(long timeDuration, TimeUnit timeUnit) {
        this(timeDuration, timeUnit, DEFAULT_WORKER_COUNT);
    }

    @Override
    protected ScheduledConsumer createConsumer() {
        return new ScheduledConsumer(pendingEvents, timeUnit, timeDuration) {
            @Override
            protected void doConsume(List<TrackEvent> events) {
                consumeEvents(events);
            }
        };
    }

    protected abstract void consumeEvents(Collection<TrackEvent> events);
}
