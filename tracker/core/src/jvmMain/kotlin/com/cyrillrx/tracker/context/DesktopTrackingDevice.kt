package com.cyrillrx.tracker.context

class DesktopTrackingDevice : TrackingDevice(
    os = "",
    model = "",
)

actual fun createTrackingDevice(): TrackingDevice = DesktopTrackingDevice()
