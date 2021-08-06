package com.cyrillrx.tracker.event;

import com.cyrillrx.tracker.context.TrackerContext;

import java.util.Map;

/**
 * @author Cyril Leroux
 *         Created on 25/04/2016.
 */
@SuppressWarnings("unused")
public class LogEvent extends TrackEvent {

    protected int severity;
    protected String tag;
    protected String message;
    protected String screen;
    protected String screenExecId;

    protected LogEvent() { }

    public int getSeverity() { return severity; }

    public String getTag() { return tag; }

    public String getMessage() { return message; }

    public String getScreen() { return screen; }

    public String getScreenExecId() { return screenExecId; }

    public static class Builder extends EventBuilder<LogEvent> {

        public Builder() { event = new LogEvent(); }

        public LogEvent build() {

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

        public Builder setSeverity(int severity) {
            event.severity = severity;
            return this;
        }

        public Builder setTag(String tag) {
            event.tag = tag;
            return this;
        }

        public Builder setMessage(String message) {
            event.message = message;
            return this;
        }

        public Builder setScreen(String screen) {
            event.screen = screen;
            return this;
        }

        public Builder setScreenExecId(String screenExecId) {
            event.screenExecId = screenExecId;
            return this;
        }
    }
}
