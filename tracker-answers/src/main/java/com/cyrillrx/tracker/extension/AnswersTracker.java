package com.cyrillrx.tracker.extension;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.AnswersEvent;
import com.crashlytics.android.answers.ContentViewEvent;
import com.crashlytics.android.answers.CustomEvent;
import com.cyrillrx.tracker.TrackerChild;
import com.cyrillrx.tracker.event.TrackEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * An Answer (Fabric) {@link TrackerChild}.
 *
 * @author Cyril Leroux
 *         Created on 11/11/2015.
 */
@SuppressWarnings("unused")
public class AnswersTracker extends TrackerChild {

    private static final String CATEGORY_SCREEN_VIEW = "screen_view";

    @Override
    protected void doTrack(TrackEvent event) {
        if (CATEGORY_SCREEN_VIEW.equals(event.getCategory())) {
            trackView(event);
        } else {
            trackCustom(event);
        }
    }

    private void trackView(TrackEvent source) {

        int attributeCount = 0;

        final ContentViewEvent contentViewEvent = new ContentViewEvent()
                .putContentName(source.getName())
                .putCustomAttribute("category", source.getCategory())
                .putCustomAttribute("createdAt", source.getCreatedAt());
        attributeCount += 3;

        if (source.getName() != null) {
            contentViewEvent.putContentName(source.getName());
            attributeCount++;
        }

        addCustomAttributes(source, contentViewEvent, attributeCount);

        Answers.getInstance().logContentView(contentViewEvent);
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

    private static void addCustomAttributes(TrackEvent source, AnswersEvent dest, int attributeCount) {

        if (attributeCount >= AnswersEvent.MAX_NUM_ATTRIBUTES) {
            return;
        }

        String key;
        String value;
        final Map<String, String> customAttributes = toStringMap(source.getCustomAttributes());
        final Set<Map.Entry<String, String>> entries = customAttributes.entrySet();

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