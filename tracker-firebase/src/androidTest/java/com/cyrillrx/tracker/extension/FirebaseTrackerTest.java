package com.cyrillrx.tracker.extension;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.cyrillrx.tracker.event.TrackEvent;

import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Cyril Leroux
 *         Created 15/11/2016.
 */
@RunWith(AndroidJUnit4.class)
public class FirebaseTrackerTest {

    public void testTracker() {

        final FirebaseTracker tracker = new FirebaseTracker(InstrumentationRegistry.getContext());

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
