package com.cyrillrx.tracker.event;

import com.cyrillrx.tracker.context.TrackerContext;

import java.util.Map;

/**
 * @author Cyril Leroux
 *         Created on 25/04/2016.
 */
public abstract class EventBuilder<Event extends TrackEvent> {

    protected Event event;

    public abstract Event build();

    public EventBuilder setContext(TrackerContext context) {
        event.context = context;
        return this;
    }

    public EventBuilder setCategory(String category) {
        event.category = category;
        return this;
    }

    public EventBuilder setName(String name) {
        event.name = name;
        return this;
    }

    public EventBuilder setSource(String source) {
        event.source = source;
        return this;
    }

    public EventBuilder putCustomAttribute(String key, Object value) {
        event.customAttributes.put(key, value);
        return this;
    }

    public <T> EventBuilder putCustomAttributes(Map<String, T> values) {
        event.customAttributes.putAll(values);
        return this;
    }
}
