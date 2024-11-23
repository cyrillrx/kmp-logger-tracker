package com.cyrillrx.tracker.context

import platform.UIKit.UIDevice

class IOSTrackingDevice : TrackingDevice(
    os = UIDevice.currentDevice.systemName(),
    model = UIDevice.currentDevice.model(),
)

actual fun createTrackingDevice(): TrackingDevice = IOSTrackingDevice()
