package com.cyrillrx.tracker.extension;

import android.content.Context
import android.os.Bundle
import com.cyrillrx.tracker.TrackerChild
import com.cyrillrx.tracker.event.TrackEvent
import com.google.firebase.analytics.FirebaseAnalytics

/**
 * A Firebase Analytics {@link TrackerChild}.
 *
 * @author Cyril Leroux
 *         Created on 11/11/2015.
 */
// Enable Firebase Debug View using "adb shell setprop debug.firebase.analytics.app com.printklub.polabox.debug"
class FirebaseTracker(context: Context) : TrackerChild("Firebase") {

    private val firebaseAnalytics: FirebaseAnalytics = FirebaseAnalytics.getInstance(context)

    override fun doTrack(event: TrackEvent) {
        event.checkEventValidity()

        val bundle = Bundle()
        bundle.addProperties(event)

        val outputName = event.getOutputName()

        firebaseAnalytics.logEvent(outputName, bundle)
    }

    companion object {
        /**
         * These max length are limitations for string contents sent via Firebase Analytics.
         *
         * - `MAX_LENGTH_EVENT_NAME` is the maximum length for the name of the event.
         * - `MAX_LENGTH_EVENT_KEY` is the maximum length for the key of a given entry in the event.
         * - `MAX_LENGTH_EVENT_VALUE` is the maximum length for the value associated to a key of a given entry in the
         * event.
         */
        private const val MAX_LENGTH_EVENT_NAME = 40
        private const val MAX_LENGTH_EVENT_KEY = 40
        private const val MAX_LENGTH_EVENT_VALUE = 100

        private fun TrackEvent.checkEventValidity() {
            if (name.length > MAX_LENGTH_EVENT_NAME) throw IllegalArgumentException("Event name is too long: `$name`")

            attributes.forEach { it.checkValidity() }
        }

        private fun Map.Entry<String, Any>.checkValidity() {
            if (key.length > MAX_LENGTH_EVENT_KEY) {
                throw IllegalArgumentException("Event custom attribute key is too long: `${key}`")
            }

            if (value.isTypeValueKept() && value.toString().length > MAX_LENGTH_EVENT_VALUE) {
                throw IllegalArgumentException("Event custom attribute value is too long: `${key}`")
            }
        }

        private fun Any.isTypeValueKept(): Boolean = when (this) {
            is String,
            is Boolean,
            is Int,
            is Long,
            is Float,
            is Double,
                // Add other types if needed
                -> true

            else -> false
        }

        private fun Bundle.addProperties(event: TrackEvent) {
            val properties = event.attributes

            properties.forEach { (key, value) ->
                when (value) {
                    is String -> putString(key, value.take(MAX_LENGTH_EVENT_VALUE))
                    is Boolean -> putBoolean(key, value)
                    is Int -> putInt(key, value)
                    is Long -> putLong(key, value)
                    is Float -> putFloat(key, value)
                    is Double -> putDouble(key, value)
                }
            }
        }

        private fun TrackEvent.getOutputName(): String {
            // Override event names here if needed
            return name
        }
    }
}
