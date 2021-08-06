package com.cyrillrx.tracker;

import com.cyrillrx.logger.Logger;
import com.cyrillrx.logger.Severity;
import com.cyrillrx.logger.extension.SystemOutLog;
import com.cyrillrx.tracker.event.TrackEvent;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayDeque;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Cyril Leroux
 *         Created on 20/04/16.
 */
public class StreamingTrackerTest {

    private static final String TAG = StreamingTracker.class.getSimpleName();

    private static final int EVENT_COUNT = 10;

    @BeforeClass
    public static void initLogger() {

        Logger.initialize();
        Logger.addChild(new SystemOutLog(Severity.VERBOSE, false));
    }

    @Test
    public void testConsistency() {

        Logger.info(TAG, "Started testConsistency");

        final DummyStreamingTracker tracker = new DummyStreamingTracker(10);

        Assert.assertTrue("Should be empty", tracker.isEmpty());

        // Track one event
        tracker.track(TestUtils.createFakeEvent(TestUtils.EVENT_CATEGORY));
        TestUtils.wait100Millis();

        Assert.assertEquals("Should be 1", 1, tracker.getEventCount());

        final List<TrackEvent> trackedEvents = tracker.trackedEvents;
        for (TrackEvent event : trackedEvents) {
            Assert.assertEquals("Should contain category " + TestUtils.EVENT_CATEGORY, TestUtils.EVENT_CATEGORY, event.getCategory());
        }
    }

    @Test
    public void testRetry() {

        final RetryTracker tracker = new RetryTracker(10, 1, new ArrayDeque<>(), TimeUnit.MILLISECONDS, 200);

        Assert.assertTrue("Should be empty", tracker.isEmpty());

        // First track should fail

        tracker.track(TestUtils.createFakeEvent(TestUtils.EVENT_CATEGORY));

        TestUtils.wait100Millis();
        Assert.assertTrue("Should be empty", tracker.isEmpty());

        // Wait for retry to be performed
        TestUtils.wait100Millis();
        TestUtils.wait100Millis();
        Assert.assertEquals("Should be 1", 1, tracker.getEventCount());
    }

    @Test
    public void testOneByOne() {

        final DummyStreamingTracker tracker = new DummyStreamingTracker(100);

        Assert.assertTrue("Should be empty", tracker.isEmpty());

        // Track EVENT_COUNT events
        TestUtils.trackEventsOneByOne(tracker, EVENT_COUNT, TestUtils.EVENT_CATEGORY);
        TestUtils.wait100Millis();
        Assert.assertEquals("Should be " + EVENT_COUNT, EVENT_COUNT, tracker.getEventCount());

        // Track EVENT_COUNT events
        TestUtils.trackEventsOneByOne(tracker, EVENT_COUNT, TestUtils.EVENT_CATEGORY);
        TestUtils.wait100Millis();
        int totalEventCount = EVENT_COUNT * 2;
        Assert.assertEquals("Should be " + totalEventCount, totalEventCount, tracker.getEventCount());
    }

    @Test
    public void testBatch() {

        System.out.println("Test started");

        final DummyStreamingTracker tracker = new DummyStreamingTracker(100);

        Assert.assertTrue("Should be empty", tracker.isEmpty());

        TestUtils.trackEventsBatch(tracker, EVENT_COUNT, TestUtils.EVENT_CATEGORY);
        TestUtils.wait100Millis();
        Assert.assertEquals("Should be " + EVENT_COUNT, EVENT_COUNT, tracker.getEventCount());

        TestUtils.trackEventsBatch(tracker, EVENT_COUNT, TestUtils.EVENT_CATEGORY);
        TestUtils.wait100Millis();
        int totalEventCount = EVENT_COUNT * 2;
        Assert.assertEquals("Should be " + totalEventCount, totalEventCount, tracker.getEventCount());
    }
}
