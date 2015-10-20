package com.cyrillrx.android.toolbox.logger;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * A LoggerChild aware of the log severity level.
 *
 * @author Cyril Leroux
 *         Created on 20/10/15
 */
public abstract class AbstractLogger implements LoggerChild {

    public static final int CRITICAL = 0;
    public static final int ERROR = 1;
    public static final int WARN = 2;
    public static final int INFO = 3;
    public static final int DEBUG = 4;
    public static final int VERBOSE = 5;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({CRITICAL, ERROR, WARN, INFO, DEBUG, VERBOSE})
    public @interface LogSeverity { }

    @LogSeverity
    private final int mSeverity;

    public AbstractLogger(@LogSeverity int severity) {
        mSeverity = severity;
    }

    @Override
    public void verbose(String tag, String message) {
        if (mSeverity < VERBOSE) { return; }
        doLogVerbose(tag, message);
    }

    @Override
    public void verbose(String tag, String message, Throwable throwable) {
        if (mSeverity < VERBOSE) { return; }
        doLogVerbose(tag, message, throwable);
    }

    @Override
    public void debug(String tag, String message) {
        if (mSeverity < DEBUG) { return; }
        doLogDebug(tag, message);
    }

    @Override
    public void debug(String tag, String message, Throwable throwable) {
        if (mSeverity < DEBUG) { return; }
        doLogDebug(tag, message, throwable);
    }

    @Override
    public void info(String tag, String message) {
        if (mSeverity < INFO) { return; }
        doLogInfo(tag, message);
    }

    @Override
    public void info(String tag, String message, Throwable throwable) {
        if (mSeverity < INFO) { return; }
        doLogInfo(tag, message, throwable);
    }

    @Override
    public void warning(String tag, String message) {
        if (mSeverity < WARN) { return; }
        doLogWarning(tag, message);
    }

    @Override
    public void warning(String tag, String message, Throwable throwable) {
        if (mSeverity < WARN) { return; }
        doLogWarning(tag, message, throwable);
    }

    @Override
    public void error(String tag, String message) {
        if (mSeverity < ERROR) { return; }
        doLogError(tag, message);
    }

    @Override
    public void error(String tag, String message, Throwable throwable) {
        if (mSeverity < ERROR) { return; }
        doLogError(tag, message, throwable);
    }

    protected abstract void doLogVerbose(String tag, String message);

    protected abstract void doLogVerbose(String tag, String message, Throwable throwable);

    protected abstract void doLogDebug(String tag, String message);

    protected abstract void doLogDebug(String tag, String message, Throwable throwable);

    protected abstract void doLogInfo(String tag, String message);

    protected abstract void doLogInfo(String tag, String message, Throwable throwable);

    protected abstract void doLogWarning(String tag, String message);

    protected abstract void doLogWarning(String tag, String message, Throwable throwable);

    protected abstract void doLogError(String tag, String message);

    protected abstract void doLogError(String tag, String message, Throwable throwable);

}
