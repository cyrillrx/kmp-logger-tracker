package com.cyrillrx.android.toolbox.logger;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Set;

/**
 * This class wraps instances the of the {@link LoggerChild} interface.
 * It allows custom logging conditions.
 *
 * @author Cyril Leroux
 *         Created on 03/09/12
 */
@SuppressWarnings("unused")
public class Logger {

    private static final String ERROR_INITIALIZE_FIRST = "Call initialize before using the Logger";
    private static Logger sInstance;

    private final Set<LoggerChild> mLoggers;

    private Toast mDebugToast;

    /**
     * @param context The application context to initialize the debug toast or null.
     */
    @SuppressLint("ShowToast")
    private Logger(Context context) {

        mLoggers = new HashSet<>();

        if (context != null) {
            mDebugToast = Toast.makeText(context, "", Toast.LENGTH_LONG);
            mDebugToast.setGravity(Gravity.TOP, 0, 50);
        }
    }

    /**
     * Initializes the Logger.<br />
     * Provides a context to enable a simple Toast used to display debug messages to the developer.
     *
     * @param context The application context to initialize the debug toast or null.
     */
    public static void initialize(Context context) { sInstance = new Logger(context); }

    /**
     * Initializes the Logger without the debug logger.
     */
    public static void initialize() { sInstance = new Logger(null); }

    /**
     * Shows a debug toast.
     *
     * @param message The message to toast.
     */
    public static void toast(String message) {
        checkInitialization();

        Toast toast = sInstance.mDebugToast;
        if (toast == null) { return; }

        toast.setText(message);
        toast.show();
    }

    public static void addChild(LoggerChild child) {
        checkInitialization();

        sInstance.mLoggers.add(child);
    }

    public static void removeChild(LoggerChild child) {
        checkInitialization();

        sInstance.mLoggers.remove(child);
    }

    public static void verbose(String tag, String message) {
        checkInitialization();

        for (LoggerChild log : sInstance.mLoggers) {
            log.verbose(tag, message);
        }
    }

    public static void verbose(String tag, String message, Throwable throwable) {
        checkInitialization();

        for (LoggerChild log : sInstance.mLoggers) {
            log.verbose(tag, message, throwable);
        }
    }

    public static void debug(String tag, String message) {
        checkInitialization();

        for (LoggerChild log : sInstance.mLoggers) {
            log.debug(tag, message);
        }
    }

    public static void debug(String tag, String message, Throwable throwable) {
        checkInitialization();

        for (LoggerChild log : sInstance.mLoggers) {
            log.debug(tag, message, throwable);
        }
    }

    public static void info(String tag, String message) {
        checkInitialization();

        for (LoggerChild log : sInstance.mLoggers) {
            log.info(tag, message);
        }
    }

    public static void info(String tag, String message, Throwable throwable) {
        checkInitialization();

        for (LoggerChild log : sInstance.mLoggers) {
            log.info(tag, message, throwable);
        }
    }

    public static void warning(String tag, String message) {
        checkInitialization();

        for (LoggerChild log : sInstance.mLoggers) {
            log.warning(tag, message);
        }
    }

    public static void warning(String tag, String message, Throwable throwable) {
        checkInitialization();

        for (LoggerChild log : sInstance.mLoggers) {
            log.warning(tag, message, throwable);
        }
    }

    public static void error(String tag, String message) {
        checkInitialization();

        for (LoggerChild log : sInstance.mLoggers) {
            log.error(tag, message);
        }
    }

    public static void error(String tag, String message, Throwable throwable) {
        checkInitialization();

        for (LoggerChild log : sInstance.mLoggers) {
            log.error(tag, message, throwable);
        }
    }

    private static void checkInitialization() {
        if (sInstance == null) {
            throw new IllegalStateException(ERROR_INITIALIZE_FIRST);
        }
    }
}
