package com.cyrillrx.android.tracker.event;

import android.text.TextUtils;

/**
 * @author Cyril Leroux
 *         Created on 26/11/2015.
 */
public class ViewEvent extends TrackEvent {

    ViewEvent() { }

    public static class Builder {

        private final ViewEvent event;

        public Builder() { event = new ViewEvent(); }

        public ViewEvent build() {
            if (TextUtils.isEmpty(event.category) ||
                    TextUtils.isEmpty(event.name)) {
                throw new IllegalStateException("Category and name are mandatory");
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