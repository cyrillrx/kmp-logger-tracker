package com.cyrillrx.tracker;

import com.cyrillrx.tracker.event.TrackEvent;

import java.util.Collection;

/**
 * A basic tracker aware of the filter to apply.<br>
 * By default, all events are tracked.
 *
 * @author Cyril Leroux
 *         Created on 11/11/2015.
 */
public abstract class TrackerChild {

    public final void track(TrackEvent event) {
        if (shouldTrack(event)) {
            doTrack(event);
        }
    }

    public void track(Collection<TrackEvent> events) {
        for (TrackEvent event : events) {
            track(event);
        }
    }

    protected boolean shouldTrack(TrackEvent event) { return true; }

    protected abstract void doTrack(TrackEvent event);
}
