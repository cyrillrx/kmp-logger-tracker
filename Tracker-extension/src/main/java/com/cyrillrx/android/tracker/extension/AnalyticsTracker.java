package com.cyrillrx.android.tracker.extension;

import android.support.annotation.NonNull;

import com.cyrillrx.android.tracker.TrackFilter;
import com.cyrillrx.android.tracker.TrackWrapper;
import com.cyrillrx.android.tracker.TrackerChild;

/**
 * @author Cyril Leroux
 *         Created on 11/11/2015.
 */
public class AnalyticsTracker extends TrackWrapper {

    public AnalyticsTracker(TrackFilter filter, @NonNull TrackerChild tracker) {
        super(tracker, filter);
    }
}
