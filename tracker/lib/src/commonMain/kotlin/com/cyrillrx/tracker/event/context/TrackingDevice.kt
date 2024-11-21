package com.cyrillrx.tracker.event.context

abstract class TrackingDevice(val os: String, val model: String)

class TrackingDeviceFactory {
    fun create(): TrackingDevice = createTrackingDevice()
}

internal expect fun createTrackingDevice(): TrackingDevice
