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
import java.util.Map;

/**
 * A {@link TrackWrapper} wrapping a Google Analytics {@link TrackerChild}.
 *
 * @author Cyril Leroux
 *         Created on 15/11/2016.
 */
@SuppressWarnings("unused")
public class GoogleAnalyticsTracker extends TrackWrapper {

    public static final String KEY_LABEL = "label";

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

            final Map<String, String> customAttributes = source.getCustomAttributes();
            if (customAttributes != null) {
                screenViewBuilder.setAll(customAttributes);
            }

            tracker.send(screenViewBuilder.build());
        }

        /**
         * Tracks an action event.<br />
         * Adds id, type and name metadata if available.
         *
         * @param source The event to forward to Google Analytics.
         */
        private void trackAction(ActionEvent source) {

            final HitBuilders.EventBuilder eventBuilder = new HitBuilders.EventBuilder()
                    .setCategory(source.getCategory())
                    .setAction(source.getAction());

            addCustomAttributes(source, eventBuilder);

            tracker.send(eventBuilder.build());
        }

        /**
         * Tracks a rating event.<br />
         * Adds name metadata if available.
         *
         * @param source The event to forward to Google Analytics.
         */
        private void trackRating(RatingEvent source) {

            final HitBuilders.EventBuilder eventBuilder = new HitBuilders.EventBuilder()
                    .setCategory(source.getCategory())
                    .setAction(source.getName())
                    .setLabel("rating")
                    .setValue(source.getRating());

            addCustomAttributes(source, eventBuilder);

            tracker.send(eventBuilder.build());
        }

        /**
         * Tracks a custom event.<br />
         * Adds name metadata if available.
         *
         * @param source The event to forward to Google Analytics.
         */
        private void trackCustom(TrackEvent source) {

            final HitBuilders.EventBuilder eventBuilder = new HitBuilders.EventBuilder()
                    .setCategory(source.getCategory())
                    .setAction(source.getName());

            addCustomAttributes(source, eventBuilder);

            tracker.send(eventBuilder.build());
        }

        private static void addCustomAttributes(TrackEvent event, HitBuilders.EventBuilder eventBuilder) {

            final Map<String, String> customAttributes = event.getCustomAttributes();
            if (customAttributes == null) { return; }

            if (customAttributes.containsKey(KEY_LABEL)) {
                eventBuilder.setLabel(customAttributes.get(KEY_LABEL));
            }

            eventBuilder.setAll(customAttributes);
        }
    }
}