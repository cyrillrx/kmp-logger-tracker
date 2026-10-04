package com.cyrillrx.tracker.context

class TrackerContext(
    var app: TrackingApp,
    var user: TrackingUser,
    val device: TrackingDevice,
    var connectivity: Connectivity,
)
