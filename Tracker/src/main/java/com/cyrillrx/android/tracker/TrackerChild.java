package com.cyrillrx.android.tracker;

import android.content.Context;

import com.cyrillrx.android.tracker.event.TrackEvent;

/**
 * @author Cyril Leroux
 *         Created on 11/11/2015.
 */
public interface TrackerChild {

    void track(Context context, TrackEvent event);
}
