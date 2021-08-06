package com.cyrillrx.tracker.event;

import com.cyrillrx.tracker.context.TrackerContext;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Cyril Leroux
 *         Created on 11/11/2015.
 */
public class TrackEvent {

    protected final long createdAt;

    /** Global context. Not directly linked to the event. */
    protected TrackerContext context;
    protected String category;
    protected String name;
    /** Source of the event (class name, or ui screen) */
    protected String source;

    protected Map<String, Object> customAttributes;

    protected TrackEvent() {
        createdAt = System.currentTimeMillis();
        customAttributes = new HashMap<>();
    }

    public TrackEvent setContext(TrackerContext context) {
        this.context = context;
        return this;
    }

    public long getCreatedAt() { return createdAt; }

    public String getCategory() { return category; }

    public String getName() { return name; }

    public String getSource() { return source; }

    public Map<String, Object> getCustomAttributes() { return customAttributes; }

    @SuppressWarnings("unchecked")
    public <T> T getCustomAttribute(String key) { return (T) customAttributes.get(key); }

    public static class Builder extends EventBuilder<TrackEvent> {

        public Builder() { event = new TrackEvent(); }

        @Override
        public TrackEvent build() {

            if (event.category == null || event.category.isEmpty()) {
                throw new IllegalStateException("Category is mandatory");
            }

            return event;
        }

        @Override
        public Builder setContext(TrackerContext context) { return (Builder) super.setContext(context); }

        @Override
        public Builder setCategory(String category) { return (Builder) super.setCategory(category); }

        @Override
        public Builder setName(String name) { return (Builder) super.setName(name); }

        @Override
        public Builder setSource(String source) { return (Builder) super.setSource(source); }

        @Override
        public Builder putCustomAttribute(String key, Object value) {
            return (Builder) super.putCustomAttribute(key, value);
        }

        @Override
        public <T> Builder putCustomAttributes(Map<String, T> values) {
            return (Builder) super.putCustomAttributes(values);
        }
    }
}
