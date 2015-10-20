package com.cyrillrx.android.toolbox.logger;

import android.support.annotation.NonNull;

/**
 * A {@link LogChild} wrapper aware of the log severity level.
 *
 * @author Cyril Leroux
 *         Created on 20/10/15
 */
public abstract class LogWrapper implements LogChild {

    @Severity.LogSeverity
    private final int mSeverity;
    private final LogChild mWrapped;

    public LogWrapper(@Severity.LogSeverity int severity, @NonNull LogChild logger) {
        mSeverity = severity;
        mWrapped = logger;
    }

    @Override
    public void verbose(String tag, String message) {
        if (mSeverity < Severity.VERBOSE) { return; }
        mWrapped.verbose(tag, message);
    }

    @Override
    public void verbose(String tag, String message, Throwable throwable) {
        if (mSeverity < Severity.VERBOSE) { return; }
        mWrapped.verbose(tag, message, throwable);
    }

    @Override
    public void debug(String tag, String message) {
        if (mSeverity < Severity.DEBUG) { return; }
        mWrapped.debug(tag, message);
    }

    @Override
    public void debug(String tag, String message, Throwable throwable) {
        if (mSeverity < Severity.DEBUG) { return; }
        mWrapped.debug(tag, message, throwable);
    }

    @Override
    public void info(String tag, String message) {
        if (mSeverity < Severity.INFO) { return; }
        mWrapped.info(tag, message);
    }

    @Override
    public void info(String tag, String message, Throwable throwable) {
        if (mSeverity < Severity.INFO) { return; }
        mWrapped.info(tag, message, throwable);
    }

    @Override
    public void warning(String tag, String message) {
        if (mSeverity < Severity.WARN) { return; }
        mWrapped.warning(tag, message);
    }

    @Override
    public void warning(String tag, String message, Throwable throwable) {
        if (mSeverity < Severity.WARN) { return; }
        mWrapped.warning(tag, message, throwable);
    }

    @Override
    public void error(String tag, String message) {
        if (mSeverity < Severity.ERROR) { return; }
        mWrapped.error(tag, message);
    }

    @Override
    public void error(String tag, String message, Throwable throwable) {
        if (mSeverity < Severity.ERROR) { return; }
        mWrapped.error(tag, message, throwable);
    }

}
