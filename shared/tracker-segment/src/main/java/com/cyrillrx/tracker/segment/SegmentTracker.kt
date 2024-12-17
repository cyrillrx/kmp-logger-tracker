package com.cyrillrx.tracker.segment

import android.content.Context
import com.cyrillrx.android.device.HardwareUtils
import com.cyrillrx.tracker.TrackerChild
import com.cyrillrx.tracker.event.TrackEvent
import com.segment.analytics.Analytics
import com.segment.analytics.Properties

/**
 * @author Cyril Leroux
 *         Created on 10/09/2019.
 */
abstract class SegmentTracker(
    private val context: Context,
    writeKey: String,
    debug: Boolean = false
) : TrackerChild() {

    init {

        val builder = Analytics.Builder(context, writeKey).apply {
            trackApplicationLifecycleEvents()
            if (debug) {
                flushQueueSize(1)
                logLevel(Analytics.LogLevel.VERBOSE)
            }
        }

        // Let analytics handle its own instance
        Analytics.setSingletonInstance(builder.build())
    }

    protected open fun transformEventName(input: String): String = input

    protected open fun trackView(event: TrackEvent) {

        val eventName = transformEventName(event.name)

        val properties = Properties()
            .apply {
                putName(eventName)
                addDeviceProperties()
                addEventAttributes(event)
            }

        Analytics.with(context).screen("", eventName, properties, null)
    }

    protected open fun trackCustom(event: TrackEvent) {

        val eventName = transformEventName(event.name)

        val properties = Properties()
            .apply {
                putName(eventName)
                addDeviceProperties()
                addEventAttributes(event)
            }

        Analytics.with(context).track(eventName, properties)
    }

    protected open fun Properties.addDeviceProperties() {
        putAll(HardwareUtils.getDeviceProperties(context))
    }

    protected open fun Properties.addEventAttributes(event: TrackEvent) {
        event.customAttributes?.let { putAll(it) }
    }
}