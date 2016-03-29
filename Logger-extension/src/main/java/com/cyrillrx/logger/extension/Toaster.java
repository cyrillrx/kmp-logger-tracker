package com.cyrillrx.logger.extension;

import android.annotation.SuppressLint;
import android.content.Context;
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

    /**
     * @param context The application context to initialize the debug toast or null.
     */
    @SuppressLint("ShowToast")
    private Toaster(Context context) {

        if (context != null) {
            mDebugToast = Toast.makeText(context, "", Toast.LENGTH_LONG);
            mDebugToast.setGravity(Gravity.TOP, 0, 50);
        }
    }

    /**
     * Initializes the Toaster.<br />
     * Provides a context to enable a simple Toast used to display debug messages to the developer.
     *
     * @param context The application context to initialize the debug toast or null.
     */
    public static void initialize(Context context) {
        checkMultiInitialization();

        sInstance = new Toaster(context);
    }

    /**
     * Shows a debug toast.
     *
     * @param message The message to toast.
     */
    public static synchronized void toast(String message) {
        checkInitialized();

        final Toast toast = sInstance.mDebugToast;
        if (toast == null) { return; }

        toast.setText(message);
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