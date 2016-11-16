package com.cyrillrx.tracker.extension;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.AnswersEvent;
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

    public AnswerTracker(TrackFilter filter) { super(new AnswersTrackChild(), filter); }

    public AnswerTracker() { super(new AnswersTrackChild()); }

    private static class AnswersTrackChild implements TrackerChild {

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

            int attributeCount = 0;

            final ContentViewEvent contentViewEvent = new ContentViewEvent()
                    .putContentName(source.getName())
                    .putCustomAttribute("category", source.getCategory())
                    .putCustomAttribute("createdAt", source.getCreatedAt());
            attributeCount += 3;

            if (source.getId() != null) {
                contentViewEvent.putContentId(source.getId());
                attributeCount++;
            }

            if (source.getType() != null) {
                contentViewEvent.putContentType(source.getType());
                attributeCount++;
            }

            if (source.getName() != null) {
                contentViewEvent.putContentName(source.getName());
                attributeCount++;
            }

            addCustomAttributes(source, contentViewEvent, attributeCount);

            Answers.getInstance().logContentView(contentViewEvent);
        }

        /**
         * Tracks an action event.<br />
         * Adds id, type and name metadata if available.
         *
         * @param source The event to forward to Fabric.
         */
        private void trackAction(ActionEvent source) {

            int attributeCount = 0;

            final CustomEvent customEvent = new CustomEvent(source.getAction())
                    .putCustomAttribute("action", source.getAction())
                    .putCustomAttribute("category", source.getCategory())
                    .putCustomAttribute("createdAt", source.getCreatedAt());
            attributeCount += 3;

            if (source.getName() != null) {
                customEvent.putCustomAttribute("name", source.getName());
                attributeCount++;
            }

            addCustomAttributes(source, customEvent, attributeCount);

            Answers.getInstance().logCustom(customEvent);
        }

        /**
         * Tracks a rating event.<br />
         * Adds name metadata if available.
         *
         * @param source The event to forward to Fabric.
         */
        private void trackRating(RatingEvent source) {

            int attributeCount = 0;

            final com.crashlytics.android.answers.RatingEvent ratingEvent = new com.crashlytics.android.answers.RatingEvent()
                    .putRating(source.getRating())
                    .putContentType(source.getType())
                    .putContentId(source.getId())
                    .putCustomAttribute("category", source.getCategory())
                    .putCustomAttribute("createdAt", source.getCreatedAt());
            attributeCount += 5;

            if (source.getName() != null) {
                ratingEvent.putCustomAttribute("name", source.getName());
                attributeCount++;
            }

            addCustomAttributes(source, ratingEvent, attributeCount);

            Answers.getInstance().logRating(ratingEvent);
        }

        /**
         * Tracks a custom event.<br />
         * Adds name metadata if available.
         *
         * @param source The event to forward to Fabric.
         */
        private void trackCustom(TrackEvent source) {

            int attributeCount = 0;

            String eventName = source.getName();
            if (eventName == null) {
                // Fallback on event category if no name is found
                eventName = source.getCategory();
            }

            final CustomEvent customEvent = new CustomEvent(eventName)
                    .putCustomAttribute("category", source.getCategory())
                    .putCustomAttribute("createdAt", source.getCreatedAt());
            attributeCount += 2;

            if (source.getName() != null) {
                customEvent.putCustomAttribute("name", source.getName());
                attributeCount++;
            }

            addCustomAttributes(source, customEvent, attributeCount);

            Answers.getInstance().logCustom(customEvent);
        }
    }

    private static void addCustomAttributes(TrackEvent source, AnswersEvent dest, int attributeCount) {

        if (attributeCount >= AnswersEvent.MAX_NUM_ATTRIBUTES) {
            return;
        }

        String key;
        String value;
        final Set<Map.Entry<String, String>> entries = source.getCustomAttributes().entrySet();

        for (Map.Entry<String, String> entry : entries) {

            // Check null values
            key = entry.getKey();
            value = entry.getValue();
            if (key == null || value == null) {
                continue;
            }

            if (value.length() > AnswersEvent.MAX_STRING_LENGTH) {
                value = value.substring(0, AnswersEvent.MAX_STRING_LENGTH);
            }
            dest.putCustomAttribute(key, value);
            attributeCount++;

            if (attributeCount == AnswersEvent.MAX_NUM_ATTRIBUTES) {
                return;
            }
        }
    }
}