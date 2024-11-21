package com.cyrillrx.tracker.event.context

import android.os.Build

class AndroidTrackingDevice : TrackingDevice(
    os = "Android",
    model = Build.MODEL.orEmpty(),
)

actual fun createTrackingDevice(): TrackingDevice = AndroidTrackingDevice()
