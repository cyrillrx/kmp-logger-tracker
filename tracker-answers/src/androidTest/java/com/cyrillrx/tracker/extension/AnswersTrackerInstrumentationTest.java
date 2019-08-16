package com.cyrillrx.tracker.extension;

import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.AnswersEvent;
import com.cyrillrx.tracker.event.TrackEvent;

import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;

import io.fabric.sdk.android.Fabric;

/**
 * @author Cyril Leroux
 *         Created 15/11/2016.
 */
@RunWith(AndroidJUnit4.class)
public class AnswersTrackerInstrumentationTest {

    public void testTracker() {

        final Fabric fabric = new Fabric.Builder(InstrumentationRegistry.getContext())
                .kits(new Crashlytics())
                .appIdentifier("com.cyrillrx.test")
                .build();
        Fabric.with(fabric);

        final AnswersTracker tracker = new AnswersTracker();

        final Map<String, Object> attributes = new HashMap<>();
        for (int i = 0; i < AnswersEvent.MAX_NUM_ATTRIBUTES + 1; i++) {
            attributes.put(String.valueOf(i), String.valueOf(i));
        }

        final TrackEvent trackEvent = new TrackEvent.Builder()
                .setCategory("test")
                .putCustomAttributes(attributes)
                .build();

        tracker.track(trackEvent);
    }
}
