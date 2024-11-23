package com.cyrillrx.tracker.context

abstract class TrackingDevice(val os: String, val model: String)

class TrackingDeviceFactory {
    fun create(): TrackingDevice = createTrackingDevice()
}

internal expect fun createTrackingDevice(): TrackingDevice
