package com.cyrillrx.notifier;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.widget.Toast;

/**
 * @author Cyril Leroux
 *         Created on 29/03/16
 */
@SuppressWarnings("unused")
public class Toaster {

    private static final String ERROR_ALREADY_INITIALIZED = "initialize() has already been called.";
    private static final String ERROR_INITIALIZE_FIRST = "Call initialize() before using the Toaster.";

    private static Toaster instance;
    private Toast debugToast;
    private Toast userToast;

    /**
     * @param context The application context to initialize the debug toast or null.
     */
    @SuppressLint("ShowToast")
    private Toaster(@NonNull Context context, boolean debug) {

        if (debug) {
            debugToast = Toast.makeText(context, "", Toast.LENGTH_LONG);
            debugToast.setGravity(Gravity.TOP, 0, 50);
        }

        userToast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
    }

    /**
     * Initializes the Toaster.<br />
     * Provides a context to enable a simple Toast used to display debug messages to the developer.
     *
     * @param context The application context to initialize the debug toast or null.
     */
    public static void initialize(@NonNull Context context, boolean debug) {
        checkMultiInitialization();

        instance = new Toaster(context, debug);
    }

    /**
     * Shows a debug toast (for the developer).
     *
     * @param message The message to toast.
     */
    public static synchronized void debug(String message) {
        checkInitialized();
        toast(instance.debugToast, message);
    }

    /**
     * Shows a debug toast (for the developer).
     *
     * @param messageRes The resource of the message to toast.
     */
    public static synchronized void debug(int messageRes) {
        checkInitialized();
        toast(instance.debugToast, messageRes);
    }

    /**
     * Shows a classical toast (for the user).
     *
     * @param message The message to toast.
     */
    public static synchronized void toast(String message) {
        checkInitialized();
        toast(instance.userToast, message);
    }

    /**
     * Shows a classical toast (for the user).
     *
     * @param messageRes The resource of the message to toast.
     */
    public static synchronized void toast(int messageRes) {
        checkInitialized();
        toast(instance.userToast, messageRes);
    }

    /**
     * Shows a classical toast (for the user).
     *
     * @param message The message to toast.
     */
    public static synchronized void toast(String message, int tempDuration) {
        checkInitialized();

        // Save the initial duration
        final int oldDuration = instance.userToast.getDuration();

        // Set the temp duration
        instance.userToast.setDuration(tempDuration);

        // Toast the message
        toast(instance.userToast, message);

        // Restore the initial duration
        instance.userToast.setDuration(oldDuration);
    }

    /**
     * Shows a classical toast (for the user).
     *
     * @param messageRes The resource of the message to toast.
     */
    public static synchronized void toast(int messageRes, int tempDuration) {
        checkInitialized();

        // Save the initial duration
        final int oldDuration = instance.userToast.getDuration();

        // Set the temp duration
        instance.userToast.setDuration(tempDuration);

        // Toast the message
        toast(instance.userToast, messageRes);

        // Restore the initial duration
        instance.userToast.setDuration(oldDuration);
    }

    /**
     * Shows a toast.
     *
     * @param message The message to toast.
     */
    private static synchronized void toast(Toast toast, String message) {
        if (toast == null) { return; }

        toast.setText(message);
        toast.show();
    }

    /**
     * Shows a toast.
     *
     * @param messageRes The resource of the message to toast.
     */
    private static synchronized void toast(Toast toast, int messageRes) {
        if (toast == null) { return; }

        toast.setText(messageRes);
        toast.show();
    }

    /**
     * Checks whether the component has been initialized.<br />
     * Throws if not.
     */
    private static void checkInitialized() {
        if (instance == null) {
            throw new IllegalStateException(ERROR_INITIALIZE_FIRST);
        }
    }

    /**
     * Prevents multiple initialization of the component.<br />
     * Throws if the component has already been initialized.
     */
    private static void checkMultiInitialization() {
        if (instance != null) {
            throw new IllegalStateException(ERROR_ALREADY_INITIALIZED);
        }
    }
}