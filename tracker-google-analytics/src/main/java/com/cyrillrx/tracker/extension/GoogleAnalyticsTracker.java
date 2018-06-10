package com.cyrillrx.tracker.extension;

import android.support.annotation.NonNull;

import com.cyrillrx.tracker.TrackFilter;
import com.cyrillrx.tracker.TrackWrapper;
import com.cyrillrx.tracker.TrackerChild;
import com.cyrillrx.tracker.event.TrackEvent;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * A {@link TrackWrapper} wrapping a Google Analytics {@link TrackerChild}.
 *
 * @author Cyril Leroux
 *         Created on 15/11/2016.
 */
@SuppressWarnings("unused")
public class GoogleAnalyticsTracker extends TrackWrapper {

    public static final String CATEGORY_SCREEN_VIEW = "screen_view";
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

            if (CATEGORY_SCREEN_VIEW.equals(event.getCategory())) {
                trackView(event);
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

        private void trackView(TrackEvent source) {

            final HitBuilders.ScreenViewBuilder eventBuilder = new HitBuilders.ScreenViewBuilder();

            tracker.setScreenName(source.getName());

            final Map<String, String> customAttributes = toStringMap(source.getCustomAttributes());
            if (!customAttributes.isEmpty()) {
                eventBuilder.setAll(customAttributes);
            }

            tracker.send(eventBuilder.build());
        }

        /**
         * Tracks a custom event.<br />
         * Adds name metadata if available.
         *
         * @param event The event to forward to Google Analytics.
         */
        private void trackCustom(TrackEvent event) {

            final String category = event.getCategory();

            final HitBuilders.EventBuilder eventBuilder = new HitBuilders.EventBuilder()
                    .setCategory(category)
                    .setLabel(event.getName());

            final Map<String, String> customAttributes = toStringMap(event.getCustomAttributes());
            if (customAttributes.isEmpty()) { return; }

            if (customAttributes.containsKey(KEY_LABEL)) {
                eventBuilder.setLabel(customAttributes.get(KEY_LABEL));
            }

            eventBuilder.setAll(customAttributes);

            tracker.send(eventBuilder.build());
        }

        @NonNull
        private static Map<String, String> toStringMap(Map<String, Object> input) {

            final Map<String, String> output = new HashMap<>();

            if (input == null) { return output; }

            for (String key : input.keySet()) {
                final Object value = input.get(key);
                if (value instanceof String) {
                    output.put(key, (String) value);
                } else {
                    // TODO serialize value instead
                    output.put(key, String.valueOf(value));
                }
            }

            return output;
        }
    }
}