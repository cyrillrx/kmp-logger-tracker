package com.cyrillrx.tracker.extension;

import com.cyrillrx.tracker.TrackFilter;
import com.cyrillrx.tracker.TrackWrapper;
import com.cyrillrx.tracker.TrackerChild;
import com.cyrillrx.tracker.event.ActionEvent;
import com.cyrillrx.tracker.event.RatingEvent;
import com.cyrillrx.tracker.event.TrackEvent;
import com.cyrillrx.tracker.event.ViewEvent;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.Collection;

/**
 * A {@link TrackWrapper} wrapping a Google Analytics {@link TrackerChild}.
 *
 * @author Cyril Leroux
 *         Created on 15/11/2016.
 */
@SuppressWarnings("unused")
public class GoogleAnalyticsTracker extends TrackWrapper {

    public GoogleAnalyticsTracker(Tracker tracker, TrackFilter filter) {
        super(new GoogleAnalyticsTrackChild(tracker), filter);
    }

    public GoogleAnalyticsTracker(Tracker tracker) {
        super(new GoogleAnalyticsTrackChild(tracker));
    }

    private static class GoogleAnalyticsTrackChild implements TrackerChild {

        private Tracker tracker;

        public GoogleAnalyticsTrackChild(Tracker tracker) {
            this.tracker = tracker;
        }

        @Override
        public void track(TrackEvent event) {

            if (event instanceof ViewEvent) {
                trackView((ViewEvent) event);

            } else if (event instanceof ActionEvent) {
                trackAction((ActionEvent) event);

            } else if (event instanceof RatingEvent) {
                trackRating((RatingEvent) event);

            } else {
                trackCustom(event);
            }
        }

        @Override
        public void track(Collection<TrackEvent> events) {
            for (TrackEvent event : events) {
                track(event);
            }
        }

        private void trackView(ViewEvent source) {

            final HitBuilders.ScreenViewBuilder screenViewBuilder = new HitBuilders.ScreenViewBuilder();

            tracker.setScreenName(source.getName());
            tracker.send(screenViewBuilder.build());
        }

        /**
         * Tracks an action event.<br />
         * Adds id, type and name metadata if available.
         *
         * @param source The event to forward to Fabric.
         */
        private void trackAction(ActionEvent source) {

            tracker.send(new HitBuilders.EventBuilder()
                    .setCategory(source.getCategory())
                    .setAction(source.getAction())
                    .build());
        }

        /**
         * Tracks a rating event.<br />
         * Adds name metadata if available.
         *
         * @param source The event to forward to Fabric.
         */
        private void trackRating(RatingEvent source) {

            tracker.send(new HitBuilders.EventBuilder()
                    .setCategory(source.getCategory())
                    .setAction(source.getName())
                    .setLabel("rating")
                    .setValue(source.getRating())
                    .build());
        }

        /**
         * Tracks a custom event.<br />
         * Adds name metadata if available.
         *
         * @param source The event to forward to Fabric.
         */
        private void trackCustom(TrackEvent source) {

            tracker.send(new HitBuilders.EventBuilder()
                    .setCategory(source.getCategory())
                    .setAction(source.getName())
                    .build());
        }
    }
}