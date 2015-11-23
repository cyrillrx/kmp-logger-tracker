package com.cyrillrx.android.tracker.extension;

import android.support.annotation.NonNull;

import com.cyrillrx.android.tracker.TrackFilter;
import com.cyrillrx.android.tracker.TrackWrapper;
import com.cyrillrx.android.tracker.TrackerChild;

/**
 * @author Cyril Leroux
 *         Created on 11/11/2015.
 */
public class FabricTracker extends TrackWrapper {

    public FabricTracker(@NonNull TrackerChild tracker, TrackFilter filter) {
        super(tracker, filter);
    }
}
