package com.cyrillrx.tracker.extension;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.crashlytics.android.answers.CustomEvent;
import com.cyrillrx.tracker.TrackFilter;
import com.cyrillrx.tracker.TrackWrapper;
import com.cyrillrx.tracker.TrackerChild;
import com.cyrillrx.tracker.event.ActionEvent;
import com.cyrillrx.tracker.event.RatingEvent;
import com.cyrillrx.tracker.event.TrackEvent;
import com.cyrillrx.tracker.event.ViewEvent;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * A {@link TrackWrapper} wrapping a Answer (Fabric) {@link TrackerChild}.
 *
 * @author Cyril Leroux
 *         Created on 11/11/2015.
 */
@SuppressWarnings("unused")
public class AnswerTracker extends TrackWrapper {

    public AnswerTracker(TrackFilter filter) { super(new FabricTrackChild(), filter); }

    public AnswerTracker() { super(new FabricTrackChild()); }

    private static class FabricTrackChild implements TrackerChild {

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

            final ContentViewEvent contentViewEvent = new ContentViewEvent()
                    .putContentName(source.getName())
                    .putCustomAttribute("category", source.getCategory())
                    .putCustomAttribute("createdAt", source.getCreatedAt());

            if (source.getId() != null) {
                contentViewEvent.putContentId(source.getId());
            }

            if (source.getType() != null) {
                contentViewEvent.putContentType(source.getType());
            }

            if (source.getName() != null) {
                contentViewEvent.putContentName(source.getName());
            }

            final Set<Map.Entry<String, String>> entries = source.getCustomAttributes().entrySet();
            for (Map.Entry<String, String> entry : entries) {
                contentViewEvent.putCustomAttribute(entry.getKey(), entry.getValue());
            }

            Answers.getInstance().logContentView(contentViewEvent);
        }

        /**
         * Tracks an action event.<br />
         * Adds id, type and name metadata if available.
         *
         * @param source The event to forward to Fabric.
         */
        private void trackAction(ActionEvent source) {

            final CustomEvent customEvent = new CustomEvent(source.getAction())
                    .putCustomAttribute("action", source.getAction())
                    .putCustomAttribute("category", source.getCategory())
                    .putCustomAttribute("createdAt", source.getCreatedAt());

            if (source.getId() != null) {
                customEvent.putCustomAttribute("id", source.getId());
            }
            if (source.getType() != null) {
                customEvent.putCustomAttribute("type", source.getType());
            }
            if (source.getName() != null) {
                customEvent.putCustomAttribute("name", source.getName());
            }

            final Set<Map.Entry<String, String>> entries = source.getCustomAttributes().entrySet();
            for (Map.Entry<String, String> entry : entries) {
                customEvent.putCustomAttribute(entry.getKey(), entry.getValue());
            }

            Answers.getInstance().logCustom(customEvent);
        }

        /**
         * Tracks a rating event.<br />
         * Adds name metadata if available.
         *
         * @param source The event to forward to Fabric.
         */
        private void trackRating(RatingEvent source) {

            final com.crashlytics.android.answers.RatingEvent ratingEvent = new com.crashlytics.android.answers.RatingEvent()
                    .putRating(source.getRating())
                    .putContentType(source.getType())
                    .putContentId(source.getId())
                    .putCustomAttribute("category", source.getCategory())
                    .putCustomAttribute("createdAt", source.getCreatedAt());

            if (source.getName() != null) {
                ratingEvent.putCustomAttribute("name", source.getName());
            }

            final Set<Map.Entry<String, String>> entries = source.getCustomAttributes().entrySet();
            for (Map.Entry<String, String> entry : entries) {
                ratingEvent.putCustomAttribute(entry.getKey(), entry.getValue());
            }

            Answers.getInstance().logRating(ratingEvent);
        }

        /**
         * Tracks a custom event.<br />
         * Adds name metadata if available.
         *
         * @param source The event to forward to Fabric.
         */
        private void trackCustom(TrackEvent source) {

            String eventName = source.getName();
            if (eventName == null) {
                // Fallback on event category if no name is found
                eventName = source.getCategory();
                if (source.getType() != null && source.getId() != null) {
                    // Add event type and id if available
                    eventName += "_" + source.getType() + "_" + source.getId();
                }
            }

            final CustomEvent customEvent = new CustomEvent(eventName)
                    .putCustomAttribute("category", source.getCategory())
                    .putCustomAttribute("createdAt", source.getCreatedAt());

            if (source.getId() != null) {
                customEvent.putCustomAttribute("id", source.getId());
            }
            if (source.getType() != null) {
                customEvent.putCustomAttribute("type", source.getType());
            }
            if (source.getName() != null) {
                customEvent.putCustomAttribute("name", source.getName());
            }

            final Set<Map.Entry<String, String>> entries = source.getCustomAttributes().entrySet();
            for (Map.Entry<String, String> entry : entries) {
                customEvent.putCustomAttribute(entry.getKey(), entry.getValue());
            }

            Answers.getInstance().logCustom(customEvent);
        }
    }
}