package com.cyrillrx.tracker.extension;

import com.cyrillrx.tracker.TrackFilter;
import com.cyrillrx.tracker.TrackWrapper;
import com.cyrillrx.tracker.TrackerChild;
import com.cyrillrx.tracker.TrackerContext;
import com.cyrillrx.tracker.event.TrackEvent;

/**
 * A {@link TrackWrapper} wrapping a Google Analytics {@link TrackerChild}.
 *
 * @author Cyril Leroux
 *         Created on 11/11/2015.
 */
public class AnalyticsTracker extends TrackWrapper {

    public AnalyticsTracker(TrackFilter filter) { super(new AnalyticsTrackChild(), filter); }

    public AnalyticsTracker() { super(new AnalyticsTrackChild()); }

    private static class AnalyticsTrackChild implements TrackerChild {

        @Override
        public void track(TrackerContext context, TrackEvent event) {
            // TODO
        }
    }
}
