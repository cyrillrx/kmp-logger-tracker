package com.cyrillrx.tracker.extension;

import android.content.Context;

import com.cyrillrx.tracker.event.TrackEvent;
import com.google.android.gms.analytics.GoogleAnalytics;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Cyril Leroux
 *         Created 17/11/2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class GoogleAnalyticsTrackerTest {

    @Mock
    Context context;

    @Test
    public void testTracker() {

        final GoogleAnalytics analytics = GoogleAnalytics.getInstance(context);
        final GoogleAnalyticsTracker tracker = new GoogleAnalyticsTracker(analytics.newTracker(null));

        final Map<String, String> attributes = new HashMap<>();
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
