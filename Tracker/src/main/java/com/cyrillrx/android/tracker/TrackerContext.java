package com.cyrillrx.android.tracker;

/**
 * @author Cyril Leroux
 *         Created on 11/11/2015.
 */
public class TrackerContext {

    private App    app;
    private User   user;
    private Device device;

    public void setApp(App app) {
        this.app = app;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

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

        public void setId(String id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

    public static class Device {

        private String os;

        private String brand;
        private String manufacturer;
        private String model;
        private String serial;
        private String display;

        public void setOs(String os) {
            this.os = os;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public void setManufacturer(String manufacturer) {
            this.manufacturer = manufacturer;
        }

        public void setModel(String model) {
            this.model = model;
        }

        public void setSerial(String serial) {
            this.serial = serial;
        }

        public void setDisplay(String display) {
            this.display = display;
        }
    }

}
