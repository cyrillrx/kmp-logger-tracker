package com.cyrillrx.android.toolbox.tracker;

import android.content.Context;

import java.util.HashSet;
import java.util.Set;

/**
 * A tool to wrap Analytics events.
 *
 * @author Cyril Leroux
 *         Created on 17/04/2015
 */
@SuppressWarnings("unused")
public class Tracker {

    private static final String TAG = Tracker.class.getSimpleName();

    private static Tracker sInstance;

    private Set<TrackerChild> mTrackers;

    private Tracker() {
        mTrackers = new HashSet<>();
    }

    public static void initialize(Context context, boolean isEnabled) {
        sInstance = new Tracker();
    }

    public static void addChild(TrackerChild child) {
        if (sInstance == null) { return; }

        sInstance.mTrackers.add(child);
    }

    public static void removeChild(TrackerChild child) {
        if (sInstance == null) { return; }

        sInstance.mTrackers.remove(child);
    }

    public static void track(String tag, String message) {
        if (sInstance == null) { return; }

        for (TrackerChild tracker : sInstance.mTrackers) {
            tracker.track(tag, message);
        }
    }
}
