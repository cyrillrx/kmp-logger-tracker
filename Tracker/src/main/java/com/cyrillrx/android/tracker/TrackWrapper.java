package com.cyrillrx.android.tracker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.cyrillrx.android.tracker.event.TrackEvent;

/**
 * A {@link TrackerChild} wrapper aware of the filter to apply.
 * If no filter is set, the tracker tracks all events.
 *
 * @author Cyril Leroux
 *         Created on 11/11/15
 */
public abstract class TrackWrapper implements TrackerChild, TrackFilter {

    private final TrackFilter  mFilter;
    private final TrackerChild mWrapped;

    public TrackWrapper(@NonNull TrackerChild tracker, @Nullable TrackFilter filter) {
        mFilter = filter;
        mWrapped = tracker;
    }

    public TrackWrapper(@NonNull TrackerChild tracker) {
        this(tracker, null);
    }

    @Override
    public void track(Context context, TrackEvent event) {
        if (shouldTrack(event)) {
            mWrapped.track(context, event);
        }
    }

    @Override
    public boolean shouldTrack(TrackEvent event) {
        return mFilter == null || mFilter.shouldTrack(event);
    }
}
