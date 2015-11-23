package com.cyrillrx.android.tracker.event;

import android.text.TextUtils;

/**
 * @author Cyril Leroux
 *         Created on 11/11/2015.
 */
public class EntityEvent extends TrackEvent {

    private String id;
    private String type;
    private String action;

    private int     intValue;
    private long    longValue;
    private float   floatValue;
    private boolean boolValue;

    EntityEvent() { }

    public String getEntityId() { return id; }

    public String getEntityType() { return type; }

    public String getAction() { return action; }

    public int getIntValue() { return intValue; }

    public long getLongValue() { return longValue; }

    public float getFloatValue() { return floatValue; }

    public boolean getBoolValue() { return boolValue; }

    public static class Builder {

        private final EntityEvent event;

        public Builder() {
            event = new EntityEvent();
        }

        public EntityEvent build() {
            if (TextUtils.isEmpty(event.id) ||
                    TextUtils.isEmpty(event.type) ||
                    TextUtils.isEmpty(event.action)) {
                throw new IllegalStateException("Id, type and action are mandatory");
            }

            return event;
        }

        public void setEntityId(String id) {
            event.id = id;
        }

        public void setEntityType(String type) {
            event.type = type;
        }

        public void setEntityAction(String action) {
            event.action = action;
        }

        public void setValue(int value) {
            event.intValue = value;
        }

        public void setValue(long value) {
            event.longValue = value;
        }

        public void setValue(float value) {
            event.floatValue = value;
        }

        public void setValue(boolean value) {
            event.boolValue = value;
        }

    }
}
