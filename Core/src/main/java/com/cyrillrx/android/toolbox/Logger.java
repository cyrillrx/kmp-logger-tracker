package com.cyrillrx.android.toolbox;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * This class class wraps Android {@link android.util.Log} class.
 * It allows custom logging conditions.
 * <p/>
 * The messages are only logged if {@link #sEnabled} is true.
 *
 * @author Cyril Leroux
 *         Created on 03/09/12
 */
@SuppressWarnings("unused")
public class Logger {

    private static final String TAG = Logger.class.getSimpleName();

    private static Toast sDebugToast;
    private static boolean sEnabled;

    /**
     * Initializes the Debug Toast.
     * It's a simple Toast used for all debug message to display to the developer.
     */
    @SuppressLint("ShowToast")
    public static void initialize(Context context, boolean isDebug) {
        sEnabled = isDebug;

        if (!sEnabled) { return; }

        sDebugToast = Toast.makeText(context, "", Toast.LENGTH_LONG);
        sDebugToast.setGravity(Gravity.TOP, 0, 50);
    }

    /**
     * Shows a debug toast.
     *
     * @param message The message to toast.
     */
    public static void toast(String message) {
        if (!sEnabled) { return; }

        if (sDebugToast != null) {
            sDebugToast.setText(message);
            sDebugToast.show();
        } else {
            android.util.Log.e(TAG, "toast() - Debug Toast hasn't been initialized ! ");
        }
    }

    /**
     * Sends a {@link android.util.Log#VERBOSE} log message.<br />
     * The messages is only sent if {@link #sEnabled} is true.
     *
     * @param tag     Used to identify the source of a log message. It usually identifies the class or activity where the log call occurs.
     * @param message The message you would like to log.
     */
    public static void verbose(String tag, String message) {
        if (sEnabled) { android.util.Log.v(tag, message); }
    }

    /**
     * Sends an {@link android.util.Log#INFO} log message.<br />
     * The messages is only sent if {@link #sEnabled} is true.
     *
     * @param tag     Used to identify the source of a log message. It usually identifies the class or activity where the log call occurs.
     * @param message The message you would like to log.
     */
    public static void info(String tag, String message) {
        if (sEnabled) { android.util.Log.i(tag, message); }
    }

    /**
     * Sends a {@link android.util.Log#DEBUG} log message.<br />
     * The messages is only sent if {@link #sEnabled} is true.
     *
     * @param tag     Used to identify the source of a log message. It usually identifies the class or activity where the log call occurs.
     * @param message The message you would like to log.
     */
    public static void debug(String tag, String message) {
        if (sEnabled) { android.util.Log.d(tag, message); }
    }

    /**
     * Sends a {@link android.util.Log#WARN} log message.<br />
     * The messages is only sent if {@link #sEnabled} is true.
     *
     * @param tag     Used to identify the source of a log message. It usually identifies the class or activity where the log call occurs.
     * @param message The message you would like to log.
     */
    public static void warning(String tag, String message) {
        if (sEnabled) { android.util.Log.w(tag, message); }
    }

    /**
     * Sends a {@link android.util.Log#WARN} log message and log the exception.<br />
     * The messages is only sent if {@link #sEnabled} is true.
     *
     * @param tag       Used to identify the source of a log message. It usually identifies the class or activity where the log call occurs.
     * @param message   The message you would like to log.
     * @param throwable An exception to log.
     */
    public static void warning(String tag, String message, Throwable throwable) {
        if (sEnabled) { android.util.Log.w(tag, message, throwable); }
    }

    /**
     * Sends an {@link android.util.Log#ERROR} log message.<br />
     * The messages is only sent if {@link #sEnabled} is true.
     *
     * @param tag     Used to identify the source of a log message. It usually identifies the class or activity where the log call occurs.
     * @param message The message you would like to log.
     */
    public static void error(String tag, String message) {
        if (sEnabled) { android.util.Log.e(tag, message); }
    }

    /**
     * Sends an {@link android.util.Log#ERROR} log message and log the exception.<br />
     * The messages is only sent if {@link #sEnabled} is true.
     *
     * @param tag       Used to identify the source of a log message. It usually identifies the class or activity where the log call occurs.
     * @param message   The message you would like to log.
     * @param throwable An exception to log.
     */
    public static void error(String tag, String message, Throwable throwable) {
        if (sEnabled) { android.util.Log.e(tag, message, throwable); }
    }
}
