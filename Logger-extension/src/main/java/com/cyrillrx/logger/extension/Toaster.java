package com.cyrillrx.logger.extension;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.widget.Toast;

/**
 * @author Cyril Leroux
 *         Created on 29/03/16
 */
public class Toaster {

    private static final String ERROR_ALREADY_INITIALIZED = "initialize() has already been called.";
    private static final String ERROR_INITIALIZE_FIRST    = "Call initialize() before using the Toaster.";

    private static Toaster sInstance;
    private        Toast   mDebugToast;
    private        Toast   mUserToast;

    /**
     * @param context The application context to initialize the debug toast or null.
     */
    @SuppressLint("ShowToast")
    private Toaster(@NonNull Context context, boolean debug) {

        if (debug) {
            mDebugToast = Toast.makeText(context, "", Toast.LENGTH_LONG);
            mDebugToast.setGravity(Gravity.TOP, 0, 50);
        }

        mUserToast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
    }

    /**
     * Initializes the Toaster.<br />
     * Provides a context to enable a simple Toast used to display debug messages to the developer.
     *
     * @param context The application context to initialize the debug toast or null.
     */
    public static void initialize(@NonNull Context context, boolean debug) {
        checkMultiInitialization();

        sInstance = new Toaster(context, debug);
    }

    /**
     * Shows a debug toast (for the developer).
     *
     * @param message The message to toast.
     */
    public static synchronized void debug(String message) {
        checkInitialized();
        toast(sInstance.mDebugToast, message);
    }

    /**
     * Shows a debug toast (for the developer).
     *
     * @param messageRes The resource of the message to toast.
     */
    public static synchronized void debug(int messageRes) {
        checkInitialized();
        toast(sInstance.mDebugToast, messageRes);
    }

    /**
     * Shows a debug toast (for the developer).
     *
     * @param message The message to toast.
     */
    public static synchronized void toast(String message) {
        checkInitialized();
        toast(sInstance.mUserToast, message);
    }

    /**
     * Shows a debug toast (for the developer).
     *
     * @param messageRes The resource of the message to toast.
     */
    public static synchronized void toast(int messageRes) {
        checkInitialized();
        toast(sInstance.mUserToast, messageRes);
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
        if (sInstance == null) {
            throw new IllegalStateException(ERROR_INITIALIZE_FIRST);
        }
    }

    /**
     * Prevents multiple initialization of the component.<br />
     * Throws if the component has already been initialized.
     */
    private static void checkMultiInitialization() {
        if (sInstance != null) {
            throw new IllegalStateException(ERROR_ALREADY_INITIALIZED);
        }
    }
}