package com.cyrillrx.tracker.context;

/**
 * @author Cyril Leroux
 *         Created on 11/11/2015.
 */
class TrackerContext(
    var app: TrackingApp,
    var user: TrackingUser,
    val device: TrackingDevice,
    var connectivity: Connectivity,
) {
    fun isValid(): Boolean = app != TrackingApp.UNDEFINED
}
