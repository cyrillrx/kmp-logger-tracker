package com.cyrillrx.android.tracker.event;

/**
 * @author Cyril Leroux
 *         Created on 11/11/2015.
 */
public class TrackEvent {

    private final long createdAt;

    public TrackEvent() {
        createdAt = System.currentTimeMillis();
    }

    public long getCreatedAt() {
        return createdAt;
    }
}
