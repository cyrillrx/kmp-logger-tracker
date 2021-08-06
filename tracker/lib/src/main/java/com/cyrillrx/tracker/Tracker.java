package com.cyrillrx.tracker;

import com.cyrillrx.tracker.context.TrackerContext;
import com.cyrillrx.tracker.event.TrackEvent;

import java.util.HashSet;
import java.util.Set;

/**
 * This class wraps instances of the {@link TrackerChild} interface.
 * It allows to customize the tracking conditions.
 *
 * @author Cyril Leroux
 *         Created on 17/04/2015
 */
@SuppressWarnings("unused")
public class Tracker {

    private static final String ERROR_ALREADY_INITIALIZED = "initialize() has already been called.";
    private static final String ERROR_INITIALIZE_FIRST = "Call initialize() before using the Tracker.";
    private static Tracker instance;

    private final Set<TrackerChild> trackers;
    private final TrackerContext context;

    private ExceptionCatcher catcher;

    protected Tracker() {
        trackers = new HashSet<>();
        context = new TrackerContext();
    }

    /**
     * Initializes the Tracker.
     */
    public static void initialize() {
        checkMultiInitialization();

        instance = new Tracker();
    }

    /**
     * @deprecated use {@link #getContext()}
     */
    @Deprecated
    public static synchronized TrackerContext getTrackerContext() { return getContext(); }

    public static synchronized TrackerContext getContext() {
        checkInitialized();

        return instance.context;
    }

    public static synchronized ExceptionCatcher setCatcher(ExceptionCatcher catcher) {
        checkInitialized();

        return instance.catcher = catcher;
    }

    public static synchronized void addChild(TrackerChild child) {
        checkInitialized();

        instance.trackers.add(child);
    }

    public static synchronized void removeChild(TrackerChild child) {
        checkInitialized();

        instance.trackers.remove(child);
    }

    public static synchronized void track(TrackEvent event) {
        checkInitialized();

        event.setContext(instance.context);

        for (TrackerChild tracker : instance.trackers) {
            try {
                tracker.track(event);
            } catch (Throwable t) {
                try {
                    instance.catcher.catchException(t);
                } catch (Exception ignored) {
                    // Prevent the catcher from throwing an exception
                }
            }
        }
    }

    /**
     * Checks whether the component has been initialized.<br />
     * Throws if not.
     */
    private static void checkInitialized() {
        if (instance == null) {
            throw new IllegalStateException(ERROR_INITIALIZE_FIRST);
        }
    }

    /**
     * Prevents multiple initialization of the component.<br />
     * Throws if the component has already been initialized.
     */
    private static void checkMultiInitialization() {
        if (instance != null) {
            throw new IllegalStateException(ERROR_ALREADY_INITIALIZED);
        }
    }

    public interface ExceptionCatcher {
        void catchException(Throwable t);
    }
}
