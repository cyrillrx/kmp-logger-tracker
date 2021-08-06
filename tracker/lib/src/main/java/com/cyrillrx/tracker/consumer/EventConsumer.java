package com.cyrillrx.tracker.consumer;

import com.cyrillrx.tracker.event.TrackEvent;

import java.util.Collection;

/**
 * @author Cyril Leroux
 *         Created on 20/04/16.
 */
public abstract class EventConsumer<EventContainer extends Collection<TrackEvent>>
        implements Runnable {

    protected static final TrackEvent STOP_EVENT = new TrackEvent.Builder()
            .setCategory("STOP_EVENT")
            .build();

    protected final EventContainer events;
    protected boolean running;

    public EventConsumer(EventContainer events) { this.events = events; }

    @Override
    public void run() {
        running = true;
        while (running) {
            consume();
        }
    }

    protected abstract void consume();
}
