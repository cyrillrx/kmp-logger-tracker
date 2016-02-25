package com.cyrillrx.android.tracker;

/**
 * @author Cyril Leroux
 *         Created on 11/11/2015.
 */
public class TrackerContext {

    private App    app;
    private User   user;
    private Device device;

    public TrackerContext setApp(App app) {
        this.app = app;
        return this;
    }

    public TrackerContext setUser(User user) {
        this.user = user;
        return this;
    }

    public TrackerContext setDevice(Device device) {
        this.device = device;
        return this;
    }

    public App getApp() { return app; }

    public User getUser() { return user; }

    public Device getDevice() { return device; }

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
    }

    public static class User {

        private String id;
        private String name;
        private String email;

        public User setId(String id) {
            this.id = id;
            return this;
        }

        public User setName(String name) {
            this.name = name;
            return this;
        }

        public User setEmail(String email) {
            this.email = email;
            return this;
        }
    }

    public static class Device {

        private String os;

        private String brand;
        private String manufacturer;
        private String model;
        private String serial;
        private String display;

        public Device setOs(String os) {
            this.os = os;
            return this;
        }

        public Device setBrand(String brand) {
            this.brand = brand;
            return this;
        }

        public Device setManufacturer(String manufacturer) {
            this.manufacturer = manufacturer;
            return this;
        }

        public Device setModel(String model) {
            this.model = model;
            return this;
        }

        public Device setSerial(String serial) {
            this.serial = serial;
            return this;
        }

        public Device setDisplay(String display) {
            this.display = display;
            return this;
        }
    }

}
