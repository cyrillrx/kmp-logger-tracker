package com.cyrillrx.tracker.extension;

import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.AnswersEvent;
import com.cyrillrx.tracker.event.TrackEvent;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

import io.fabric.sdk.android.Fabric;

/**
 * @author Cyril Leroux
 *         Created 17/11/2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class AnswerTrackerTest {

    @Mock
    Context context;

    @Test
    public void testTracker() {

        final Fabric fabric = new Fabric.Builder(context)
                .kits(new Crashlytics())
                .appIdentifier("com.cyrillrx.test")
                .build();
        Fabric.with(fabric);

        final AnswerTracker tracker = new AnswerTracker();

        final Map<String, String> attributes = new HashMap<>();
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
