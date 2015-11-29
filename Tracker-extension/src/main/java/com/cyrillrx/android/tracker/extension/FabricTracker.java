package com.cyrillrx.android.tracker.extension;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.crashlytics.android.answers.CustomEvent;
import com.cyrillrx.android.tracker.TrackFilter;
import com.cyrillrx.android.tracker.TrackWrapper;
import com.cyrillrx.android.tracker.TrackerChild;
import com.cyrillrx.android.tracker.TrackerContext;
import com.cyrillrx.android.tracker.event.ActionEvent;
import com.cyrillrx.android.tracker.event.RatingEvent;
import com.cyrillrx.android.tracker.event.TrackEvent;
import com.cyrillrx.android.tracker.event.ViewEvent;

/**
 * A {@link TrackWrapper} wrapping a Answer (Fabric) {@link TrackerChild}.
 *
 * @author Cyril Leroux
 *         Created on 11/11/2015.
 */
public class FabricTracker extends TrackWrapper {

    public FabricTracker(TrackFilter filter) { super(new FabricTrackChild(), filter); }

    public FabricTracker() { super(new FabricTrackChild()); }

    private static class FabricTrackChild implements TrackerChild {

        @Override
        public void track(TrackerContext context, TrackEvent event) {

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

        private void trackView(ViewEvent event) {

            final ContentViewEvent contentViewEvent = new ContentViewEvent()
                    .putContentName(event.getName())
                    .putCustomAttribute("category", event.getCategory())
                    .putCustomAttribute("createdAt", event.getCreatedAt());

            if (event.getId() != null) {
                contentViewEvent.putCustomAttribute("id", event.getId());
            }
            if (event.getType() != null) {
                contentViewEvent.putCustomAttribute("type", event.getType());
            }

            Answers.getInstance().logContentView(contentViewEvent);
        }

        /**
         * Tracks an action event.<br />
         * Adds id, type and name metadata if available.
         *
         * @param event The event to forward to Fabric.
         */
        private void trackAction(ActionEvent event) {

            final CustomEvent customEvent = new CustomEvent(event.getAction())
                    .putCustomAttribute("action", event.getAction())
                    .putCustomAttribute("category", event.getCategory())
                    .putCustomAttribute("createdAt", event.getCreatedAt());

            if (event.getId() != null) {
                customEvent.putCustomAttribute("id", event.getId());
            }
            if (event.getType() != null) {
                customEvent.putCustomAttribute("type", event.getType());
            }
            if (event.getName() != null) {
                customEvent.putCustomAttribute("name", event.getName());
            }

            Answers.getInstance().logCustom(customEvent);
        }

        /**
         * Tracks a rating event.<br />
         * Adds name metadata if available.
         *
         * @param event The event to forward to Fabric.
         */
        private void trackRating(RatingEvent event) {

            final com.crashlytics.android.answers.RatingEvent ratingEvent = new com.crashlytics.android.answers.RatingEvent()
                    .putRating(event.getRating())
                    .putContentType(event.getType())
                    .putContentId(event.getId())
                    .putCustomAttribute("category", event.getCategory())
                    .putCustomAttribute("createdAt", event.getCreatedAt());

            if (event.getName() != null) {
                ratingEvent.putCustomAttribute("name", event.getName());
            }

            Answers.getInstance().logRating(ratingEvent);
        }

        /**
         * Tracks a custom event.<br />
         * Adds name metadata if available.
         *
         * @param event The event to forward to Fabric.
         */
        private void trackCustom(TrackEvent event) {

            String eventName = event.getName();
            if (eventName == null) {
                // Fallback on event category if no name is found
                eventName = event.getCategory();
                if (event.getType() != null && event.getId() != null) {
                    // Add event type and id if available
                    eventName += "_" + event.getType() + "_" + event.getId();
                }
            }

            final CustomEvent customEvent = new CustomEvent(eventName)
                    .putCustomAttribute("category", event.getCategory())
                    .putCustomAttribute("createdAt", event.getCreatedAt());

            if (event.getId() != null) {
                customEvent.putCustomAttribute("id", event.getId());
            }
            if (event.getType() != null) {
                customEvent.putCustomAttribute("type", event.getType());
            }
            if (event.getName() != null) {
                customEvent.putCustomAttribute("name", event.getName());
            }

            Answers.getInstance().logCustom(customEvent);
        }
    }
}