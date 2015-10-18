package com.cyrillrx.android.toolbox.logger;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Set;

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

    private static Logger sInstance;
    private static boolean sEnabled;

    private Toast mDebugToast;
    private Set<LoggerChild> mLoggers;

    @SuppressLint("ShowToast")
    private Logger(Context context, boolean isEnabled) {
        mDebugToast = Toast.makeText(context, "", Toast.LENGTH_LONG);
        mDebugToast.setGravity(Gravity.TOP, 0, 50);

        mLoggers = new HashSet<>();
    }

    /**
     * Initializes the Debug Toast.
     * It's a simple Toast used for all debug message to display to the developer.
     */
    public static void initialize(Context context, boolean isEnabled) {
        sEnabled = isEnabled;

        if (!sEnabled) { return; }

        sInstance = new Logger(context, true);
        sInstance.mLoggers.add(new LogCat());
    }

    /**
     * Shows a debug toast.
     *
     * @param message The message to toast.
     */
    public static void toast(String message) {
        if (!sEnabled) { return; }

        sInstance.mDebugToast.setText(message);
        sInstance.mDebugToast.show();
    }

    public static void addChild(LoggerChild child) {
        if (!sEnabled) { return; }

        sInstance.mLoggers.add(child);
    }

    public static void removeChild(LoggerChild child) {
        if (!sEnabled) { return; }

        sInstance.mLoggers.remove(child);
    }

    public static void verbose(String tag, String message) {
        if (!sEnabled) { return; }

        for (LoggerChild log : sInstance.mLoggers) {
            log.verbose(tag, message);
        }
    }

    public static void verbose(String tag, String message, Throwable throwable) {
        if (!sEnabled) { return; }

        for (LoggerChild log : sInstance.mLoggers) {
            log.verbose(tag, message, throwable);
        }
    }

    public static void debug(String tag, String message) {
        if (!sEnabled) { return; }

        for (LoggerChild log : sInstance.mLoggers) {
            log.debug(tag, message);
        }
    }

    public static void debug(String tag, String message, Throwable throwable) {
        if (!sEnabled) { return; }

        for (LoggerChild log : sInstance.mLoggers) {
            log.debug(tag, message, throwable);
        }
    }

    public static void info(String tag, String message) {
        if (!sEnabled) { return; }

        for (LoggerChild log : sInstance.mLoggers) {
            log.info(tag, message);
        }
    }

    public static void info(String tag, String message, Throwable throwable) {
        if (!sEnabled) { return; }

        for (LoggerChild log : sInstance.mLoggers) {
            log.info(tag, message, throwable);
        }
    }

    public static void warning(String tag, String message) {
        if (!sEnabled) { return; }

        for (LoggerChild log : sInstance.mLoggers) {
            log.warning(tag, message);
        }
    }

    public static void warning(String tag, String message, Throwable throwable) {
        if (!sEnabled) { return; }

        for (LoggerChild log : sInstance.mLoggers) {
            log.warning(tag, message, throwable);
        }
    }

    public static void error(String tag, String message) {
        if (!sEnabled) { return; }

        for (LoggerChild log : sInstance.mLoggers) {
            log.error(tag, message);
        }
    }

    public static void error(String tag, String message, Throwable throwable) {
        if (!sEnabled) { return; }

        for (LoggerChild log : sInstance.mLoggers) {
            log.error(tag, message, throwable);
        }
    }
}
