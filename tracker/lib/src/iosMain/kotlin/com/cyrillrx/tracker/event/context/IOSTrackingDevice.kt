package com.cyrillrx.tracker.event.context

import platform.UIKit.UIDevice
import com.cyrillrx.tracker.event.context.TrackingDevice

class IOSTrackingDevice : TrackingDevice(
    os = UIDevice.currentDevice.systemName(),
    model = UIDevice.currentDevice.model(),
)

actual fun createTrackingDevice(): TrackingDevice = IOSTrackingDevice()
