package com.cyrillrx.android.tracker.extension;

import android.content.Context;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.crashlytics.android.answers.RatingEvent;
import com.cyrillrx.android.tracker.TrackFilter;
import com.cyrillrx.android.tracker.TrackWrapper;
import com.cyrillrx.android.tracker.TrackerChild;
import com.cyrillrx.android.tracker.event.EntityEvent;
import com.cyrillrx.android.tracker.event.TrackEvent;

/**
 * @author Cyril Leroux
 *         Created on 11/11/2015.
 */
public class FabricTracker extends TrackWrapper {

    public FabricTracker(TrackFilter filter) {
        super(new FabricTrackChild(), filter);
    }

    private static class FabricTrackChild implements TrackerChild {

        @Override
        public void track(Context context, TrackEvent event) {
            if (event instanceof EntityEvent) {
                trackRating((EntityEvent) event);
            } else {
                track(event);
            }
        }

        private void track(TrackEvent event) {
            Answers.getInstance().logCustom(new CustomEvent(event.getName()));
        }

        private void trackRating(EntityEvent event) {
            Answers.getInstance().logRating(new RatingEvent()
                    .putRating(event.getIntValue())
                    .putContentName(event.getName())
                    .putContentType(event.getType())
                    .putContentId(event.getId()));
        }
    }
}