package com.cyrillrx.tracker.context;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Cyril Leroux
 *         Created on 11/11/2015.
 */
public class TrackerContext {

    private App app;
    private User user;
    private Device device;
    private String userAgent;

    private Set<UserChangedListener> listeners;

    private Map<String, String> customAttributes;

    public TrackerContext() {
        listeners = new HashSet<>();
        customAttributes = new HashMap<>();
    }

    public App getApp() { return app; }

    public TrackerContext setApp(App app) {
        this.app = app;
        return this;
    }

    public User getUser() { return user; }

    public TrackerContext setUser(User user) {
        this.user = user;
        for (UserChangedListener listener : listeners) {
            listener.onUserChanged(user);
        }
        return this;
    }

    public Device getDevice() { return device; }

    public TrackerContext setDevice(Device device) {
        this.device = device;
        return this;
    }

    public String getUserAgent() { return userAgent; }

    public TrackerContext setUserAgent(String userAgent) {
        this.userAgent = userAgent;
        return this;
    }

    /**
     * Adds a listener that will be notified when {@link User} is updated.
     */
    public void addListener(UserChangedListener listener) { listeners.add(listener); }

    public Map<String, String> getCustomAttributes() { return customAttributes; }

    public void putCustomAttribute(String key, String value) { customAttributes.put(key, value); }

    public void putCustomAttributes(Map<String, String> values) { customAttributes.putAll(values); }

    //
    // Inner classes
    //

    public static class App {
        private String name;
        private String version;

        public App(String name, String version) {
            this.name = name;
            this.version = version;
        }

        public String getName() { return name; }

        public String getVersion() { return version; }
    }

    public static class User {

        private String id;
        private String name;
        private String email;

        public String getId() { return id; }

        public User setId(String id) {
            this.id = id;
            return this;
        }

        public String getName() { return name; }

        public User setName(String name) {
            this.name = name;
            return this;
        }

        public String getEmail() { return email; }

        public User setEmail(String email) {
            this.email = email;
            return this;
        }
    }

    public static class Device {

        private String os;
        private String osVersion;

        private String brand;
        private String manufacturer;
        private String model;
        private String serial;
        private String display;
        private Connectivity connectivity;

        public String getOs() { return os; }

        public Device setOs(String os) {
            this.os = os;
            return this;
        }

        public String getOsVersion() { return osVersion; }

        public Device setOsVersion(String osVersion) {
            this.osVersion = osVersion;
            return this;
        }

        public String getBrand() { return brand; }

        public Device setBrand(String brand) {
            this.brand = brand;
            return this;
        }

        public String getManufacturer() { return manufacturer; }

        public Device setManufacturer(String manufacturer) {
            this.manufacturer = manufacturer;
            return this;
        }

        public String getModel() { return model; }

        public Device setModel(String model) {
            this.model = model;
            return this;
        }

        public String getSerial() { return serial; }

        public Device setSerial(String serial) {
            this.serial = serial;
            return this;
        }

        public String getDisplay() { return display; }

        public Device setDisplay(String display) {
            this.display = display;
            return this;
        }

        public Connectivity getConnectivity() { return connectivity; }

        public Device setConnectivity(Connectivity connectivity) {
            this.connectivity = connectivity;
            return this;
        }
    }

    public interface UserChangedListener {
        void onUserChanged(TrackerContext.User user);
    }

}
