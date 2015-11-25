package com.cyrillrx.android.tracker;

import com.cyrillrx.android.tracker.event.TrackEvent;

import java.util.HashSet;
import java.util.Set;

/**
 * This class wraps instances of the {@link TrackerChild} interface.<br />
 * It allows to customize the tracking conditions.
 *
 * @author Cyril Leroux
 *         Created on 17/04/2015
 */
@SuppressWarnings("unused")
public class Tracker {

    private static final String ERROR_ALREADY_INITIALIZED = "initialize() has already been called.";
    private static final String ERROR_INITIALIZE_FIRST    = "Call initialize() before using the Tracker.";
    private static Tracker sInstance;

    private final Set<TrackerChild> mTrackers;
    private final TrackerContext    mContext;

    private Tracker() {
        mTrackers = new HashSet<>();
        mContext = new TrackerContext();
    }

    /**
     * Initializes the Tracker.
     */
    public static void initialize() {
        checkMultiInitialization();

        sInstance = new Tracker();
    }

    public static synchronized TrackerContext getTrackerContext() {
        checkInitialized();

        return sInstance.mContext;
    }

    public static synchronized void addChild(TrackerChild child) {
        checkInitialized();

        sInstance.mTrackers.add(child);
    }

    public static synchronized void removeChild(TrackerChild child) {
        checkInitialized();

        sInstance.mTrackers.remove(child);
    }

    public static synchronized void track(TrackEvent event) {
        checkInitialized();

        for (TrackerChild tracker : sInstance.mTrackers) {
            tracker.track(sInstance.mContext, event);
        }
    }

    /**
     * Checks whether the component has been initialized.<br />
     * Throws if not.
     */
    private static void checkInitialized() {
        if (sInstance == null) {
            throw new IllegalStateException(ERROR_INITIALIZE_FIRST);
        }
    }

    /**
     * Prevents multiple initialization of the component.<br />
     * Throws if the component has already been initialized.
     */
    private static void checkMultiInitialization() {
        if (sInstance != null) {
            throw new IllegalStateException(ERROR_ALREADY_INITIALIZED);
        }
    }
}
