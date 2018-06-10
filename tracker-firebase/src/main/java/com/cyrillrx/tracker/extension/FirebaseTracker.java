package com.cyrillrx.tracker.extension;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.cyrillrx.tracker.TrackFilter;
import com.cyrillrx.tracker.TrackWrapper;
import com.cyrillrx.tracker.TrackerChild;
import com.cyrillrx.tracker.event.TrackEvent;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A {@link TrackWrapper} wrapping a Firebase Analytics {@link TrackerChild}.
 *
 * @author Cyril Leroux
 *         Created on 11/11/2015.
 */
@SuppressWarnings("unused")
public class FirebaseTracker extends TrackWrapper {

    public FirebaseTracker(Context context, TrackFilter filter) {
        super(new FirebaseTrackChild(FirebaseAnalytics.getInstance(context)), filter);
    }

    public FirebaseTracker(Context context) {
        super(new FirebaseTrackChild(FirebaseAnalytics.getInstance(context)));
    }

    private static class FirebaseTrackChild implements TrackerChild {

        private final FirebaseAnalytics firebaseAnalytics;

        public FirebaseTrackChild(FirebaseAnalytics firebaseAnalytics) {
            this.firebaseAnalytics = firebaseAnalytics;
        }

        @Override
        public void track(TrackEvent event) {

            final Bundle bundle = new Bundle();

            final Map<String, String> customAttributes = toStringMap(event.getCustomAttributes());
            final Set<Map.Entry<String, String>> entries = customAttributes.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                bundle.putString(entry.getKey(), entry.getValue());
            }
            firebaseAnalytics.logEvent(event.getName(), bundle);
        }

        @Override
        public void track(Collection<TrackEvent> events) {
            for (TrackEvent event : events) {
                track(event);
            }
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
