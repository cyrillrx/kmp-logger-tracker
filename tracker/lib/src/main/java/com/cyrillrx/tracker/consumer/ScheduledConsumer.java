package com.cyrillrx.tracker.consumer;

import com.cyrillrx.tracker.event.TrackEvent;
import com.cyrillrx.tracker.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

/**
 * @author Cyril Leroux
 *         Created on 21/04/16.
 */
public abstract class ScheduledConsumer extends EventConsumer<Queue<TrackEvent>> {

    private TimeUnit timeUnit;
    private long timeDuration;

    public ScheduledConsumer(Queue<TrackEvent> queue, TimeUnit unit, long duration) {
        super(queue);
        timeUnit = unit;
        timeDuration = duration;
    }

    protected void consume() {

        // Events to be sent
        final List<TrackEvent> pendingEvents = new ArrayList<>();

        TrackEvent event;
        while (!events.isEmpty()) {
            event = events.poll();

            // If stop received, stop the batch and send pending events
            if (STOP_EVENT.equals(event)) {
                running = false;
                break;
            }
            pendingEvents.add(event);
        }

        try {
            doConsume(pendingEvents);

        } catch (Exception e) {

            // Retry policy
            // TODO log warning
            events.addAll(pendingEvents);
        }

        Utils.wait(timeDuration, timeUnit);
    }

    protected abstract void doConsume(List<TrackEvent> events);
}
