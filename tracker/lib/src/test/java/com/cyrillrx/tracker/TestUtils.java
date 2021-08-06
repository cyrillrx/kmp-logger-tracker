package com.cyrillrx.tracker;

import com.cyrillrx.tracker.context.TrackerContext;
import com.cyrillrx.tracker.event.TrackEvent;
import com.cyrillrx.tracker.utils.Utils;

import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Cyril Leroux
 *         Created on 25/04/16.
 */
public class TestUtils {

    public static final String EVENT_CATEGORY = "ScreenView";
    public static final String EVENT_NAME = "Home screen view";
    public static final String EVENT_SOURCE = "AppClassOrScreen";

    public static final String KEY_1 = "key_custom_1";
    public static final String KEY_2 = "key_custom_2";
    public static final String VALUE_1 = "hello";
    public static final String VALUE_2 = "world";

    public static TrackerContext createFakeContext() {
        return new TrackerContext()
                .setApp(new TrackerContext.App("AwesomeApp", "2.0"))
                .setUser(new TrackerContext.User()
                        .setId("john_doe")
                        .setName("John Doe")
                        .setEmail("john_doe@company.com"))
                .setDevice(new TrackerContext.Device());
    }

    static TrackEvent createFakeEvent(String category) {
        return new TrackEvent.Builder().setCategory(category).build();
    }

    static void trackEventsOneByOne(TrackerChild tracker, int count, String category) {
        for (int i = 0; i < count; i++) {
            tracker.track(createFakeEvent(category));
        }
    }

    static void trackEventsBatch(TrackerChild tracker, int count, String category) {

        final List<TrackEvent> eventBucket = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            eventBucket.add(createFakeEvent(category));
        }
        tracker.track(eventBucket);
    }

    static void wait100Millis() { Utils.wait(100, TimeUnit.MILLISECONDS); }

    public static void assertTrackEventConsistency(TrackEvent event) {

        Assert.assertEquals("Event category is inconsistent.", TestUtils.EVENT_CATEGORY, event.getCategory());
        Assert.assertEquals("Event name is inconsistent.", TestUtils.EVENT_NAME, event.getName());
        Assert.assertEquals("Event source is inconsistent.", TestUtils.EVENT_SOURCE, event.getSource());

        final Map<String, Object> customAttributes = event.getCustomAttributes();
        Assert.assertEquals("Custom attr 1 is inconsistent.", VALUE_1, customAttributes.get(TestUtils.KEY_1));
        Assert.assertEquals("Custom attr 2 is inconsistent.", VALUE_2, customAttributes.get(TestUtils.KEY_2));
    }
}
