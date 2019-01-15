package com.cyrillrx.tracker.extension;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.cyrillrx.tracker.event.TrackEvent;
import com.google.android.gms.analytics.GoogleAnalytics;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Cyril Leroux
 *         Created 15/11/2016.
 */
@RunWith(AndroidJUnit4.class)
public class GoogleAnalyticsTrackerInstrumentationTest {

    @Test
    public void testTracker() {

        final GoogleAnalytics analytics = GoogleAnalytics.getInstance(InstrumentationRegistry.getContext());
        final GoogleAnalyticsTracker tracker = new GoogleAnalyticsTracker(analytics.newTracker(null));

        final Map<String, Object> attributes = new HashMap<>();
        for (int i = 0; i < 25; i++) {
            attributes.put(String.valueOf(i), String.valueOf(i));
        }

        final TrackEvent trackEvent = new TrackEvent.Builder()
                .setCategory("test")
                .putCustomAttributes(attributes)
                .build();

        tracker.track(trackEvent);
    }
}
