package com.cyrillrx.android.tracker;

import android.content.Context;

import com.cyrillrx.android.tracker.event.TrackEvent;

import java.util.HashSet;
import java.util.Set;

/**
 * This class wraps instances of the {@link TrackerChild} interface.<br />
 * It allows to customize the tracking conditions.
 *
 * @author Cyril Leroux
 *         Created on 11/11/15
 */
@SuppressWarnings("unused")
public class Tracker {

    private static final String ERROR_ALREADY_INITIALIZED = "initialize() has already been called.";
    private static final String ERROR_INITIALIZE_FIRST = "Call initialize() before using the Tracker.";
    private static Tracker sInstance;

    private final Set<TrackerChild> mTrackers;
    private TrackerContext mContext;

    /**
     * @param context The application context.
     */
    private Tracker(Context context) {
        mTrackers = new HashSet<>();
    }

    /**
     * Initializes the Tracker.
     *
     * @param context The application context to initialize the debug toast or null.
     */
    public static void initialize(Context context) {
        checkMultiInitialization();

        sInstance = new Tracker(context);
    }

    public static synchronized void addChild(TrackerChild child) {
        checkInitialized();

        sInstance.mTrackers.add(child);
    }

    public static synchronized void removeChild(TrackerChild child) {
        checkInitialized();

        sInstance.mTrackers.remove(child);
    }

    public static synchronized void track(Context context, TrackEvent event) {
        checkInitialized();

        for (TrackerChild tracker : sInstance.mTrackers) {
            tracker.track(context, event);
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
