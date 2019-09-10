package com.cyrillrx.tracker.amplitude

import android.app.Application
import android.content.Context
import com.amplitude.api.Amplitude
import com.amplitude.api.Identify
import com.cyrillrx.android.device.HardwareUtils
import com.cyrillrx.logger.Logger
import com.cyrillrx.tracker.TrackerChild
import com.cyrillrx.tracker.event.TrackEvent
import org.json.JSONException
import org.json.JSONObject

/**
 * @author Cyril Leroux
 *          Created on 08/06/2018.
 */
class AmplitudeTracker(app: Application, apiKey: String) : TrackerChild() {

    init {
        Amplitude
            .getInstance()
            .initialize(app, apiKey)
            .enableForegroundTracking(app)
    }

    override fun doTrack(event: TrackEvent?) {

        val name = event?.name ?: return
        val properties = JSONObject()

        try {
            properties.put("category", event.category)
            properties.put("source", event.source)
            properties.put("created_at", event.createdAt)
        } catch (exception: JSONException) {
        }

        event.customAttributes.forEach {
            try {
                properties.put(it.key, it.value)
            } catch (exception: JSONException) {
            }
        }

        Amplitude.getInstance().logEvent(name, properties)
    }

    companion object {
        private val TAG = AmplitudeTracker::class.java.simpleName

        private const val USER_ID = "user_id"
        private const val EMAIL = "email"

        fun identify(userId: String?) {
            val amplitude = Amplitude.getInstance()
            userId?.let { amplitude.userId = it }
            amplitude.identify(Identify())
        }

        fun identifyUserAndDevice(context: Context, userId: String?, email: String?) {

            try {
                val amplitude = Amplitude.getInstance()
                val identify = Identify().addDeviceProperties(context)

                userId?.let { id ->
                    amplitude.userId = id
                    identify.set(USER_ID, id)
                }
                email?.let { identify.set(EMAIL, it) }

                amplitude.identify(identify)

            } catch (e: Exception) {
                Logger.error(TAG, "Error while setting the user $userId - $email", e)
            }
        }

        fun identifyDevice(context: Context) {
            try {
                Amplitude.getInstance().identify(Identify().addDeviceProperties(context))
            } catch (e: Exception) {
                Logger.error(TAG, "Error while setting device data", e)
            }
        }

        fun logout() {
            val amplitude = Amplitude.getInstance()
            amplitude.userId = null
            amplitude.regenerateDeviceId()
        }

        private fun Identify.addDeviceProperties(context: Context): Identify {

            HardwareUtils.getDeviceProperties(context)
                .forEach { entry -> set(entry.key, entry.value) }
            return this
        }
    }
}