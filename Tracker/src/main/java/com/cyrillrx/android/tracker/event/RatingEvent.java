package com.cyrillrx.android.tracker.event;

import android.text.TextUtils;

import java.util.Map;

/**
 * @author Cyril Leroux
 *         Created on 11/11/2015.
 */
public class RatingEvent extends TrackEvent {

    protected int rating;

    RatingEvent() { }

    public int getRating() { return rating; }

    public static class Builder {

        private final RatingEvent event;

        private boolean isRated;

        public Builder() {
            event = new RatingEvent();
            isRated = false;
        }

        public RatingEvent build() {
            if (TextUtils.isEmpty(event.category) ||
                    TextUtils.isEmpty(event.id) ||
                    TextUtils.isEmpty(event.type) ||
                    !isRated) {
                throw new IllegalStateException("Category, id, type and rating are mandatory");
            }

            return event;
        }

        public Builder setCategory(String category) {
            event.category = category;
            return this;
        }

        public Builder setId(String id) {
            event.id = id;
            return this;
        }

        public Builder setType(String type) {
            event.type = type;
            return this;
        }

        public Builder setName(String name) {
            event.name = name;
            return this;
        }

        public Builder setRating(int rating) {
            isRated = true;
            event.rating = rating;
            return this;
        }

        public Builder putCustomAttribute(String key, String value) {
            event.customAttributes.put(key, value);
            return this;
        }

        public Builder putCustomAttributes(Map<String, String> values) {
            event.customAttributes.putAll(values);
            return this;
        }
    }
}