package com.cyrillrx.android.tracker.event;

/**
 * @author Cyril Leroux
 *         Created on 11/11/2015.
 */
public class ContentViewEvent implements TrackEvent{

    public enum Type { VIEW, ACTION }

    protected String id;
    protected String type;
    protected String name;

    public String getId() { return id; }

    public String getType() { return type; }

    public String getName() { return name; }
}
