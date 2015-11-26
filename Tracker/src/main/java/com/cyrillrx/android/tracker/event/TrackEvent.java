package com.cyrillrx.android.tracker.event;

import android.text.TextUtils;

/**
 * @author Cyril Leroux
 *         Created on 11/11/2015.
 */
public class TrackEvent {

    protected final long createdAt;

    protected String category;
    protected String id;
    protected String type;
    protected String name;

    TrackEvent() {
        createdAt = System.currentTimeMillis();
    }

    public long getCreatedAt() { return createdAt; }

    public String getCategory() { return category; }

    public String getId() { return id; }

    public String getType() { return type; }

    public String getName() { return name; }

    public static class Builder {

        private final TrackEvent event;

        public Builder() {
            event = new TrackEvent();
        }

        public TrackEvent build() {
            if (TextUtils.isEmpty(event.category)) {
                throw new IllegalStateException("Category is mandatory");
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

    }
}
