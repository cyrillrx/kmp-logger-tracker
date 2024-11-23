package com.cyrillrx.tracker

import com.cyrillrx.tracker.event.TrackEvent
import com.cyrillrx.tracker.utils.Utils

/**
 * @author Cyril Leroux
 * Created on 25/04/16.
 */
object TestUtils {
    const val EVENT_NAME: String = "Home screen view"

    fun trackEventsOneByOne(tracker: TrackerChild, count: Int, eventName: String) {
        for (i in 0 until count) {
            tracker.track(TrackEvent(eventName))
        }
    }

    fun wait100Millis() {
        Utils.wait(100, java.util.concurrent.TimeUnit.MILLISECONDS)
    }
}
