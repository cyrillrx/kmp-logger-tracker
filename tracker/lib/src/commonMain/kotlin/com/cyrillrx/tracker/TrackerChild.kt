package com.cyrillrx.tracker;

import com.cyrillrx.tracker.event.TrackEvent
import com.cyrillrx.tracker.context.Connectivity
import com.cyrillrx.tracker.context.TrackingUser

/**
 * A basic tracker able to filter events before processing them.
 * By default, all events are processed.
 *
 * @author Cyril Leroux
 *         Created on 11/11/2015.
 */
abstract class TrackerChild(val name: String) {

    fun track(event: TrackEvent) {
        if (shouldTrack(event)) {
            doTrack(event)
        }
    }

    open fun shouldTrack(event: TrackEvent): Boolean = true

    open fun onUserUpdated(user: TrackingUser) {}
    open fun onConnectivityChanged(connectivity: Connectivity) {}
    open fun onTrackerRemoved() {}

    protected abstract fun doTrack(event: TrackEvent)
}
