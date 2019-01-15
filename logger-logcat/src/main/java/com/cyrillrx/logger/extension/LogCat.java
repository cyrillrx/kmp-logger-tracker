package com.cyrillrx.logger.extension;

import android.util.Log;

import com.cyrillrx.logger.LogChild;
import com.cyrillrx.logger.LogHelper;
import com.cyrillrx.logger.Severity;
import com.cyrillrx.logger.SeverityLogChild;

/**
 * A ready-to-use severity-aware {@link LogChild} wrapping {@link Log} class.
 *
 * @author Cyril Leroux
 *         Created on 18/10/2015.
 */
@SuppressWarnings("unused")
public class LogCat extends SeverityLogChild {

    private final boolean detailedLogs;

    public LogCat(int severity, boolean detailedLogs) {
        super(severity);
        this.detailedLogs = detailedLogs;
    }

    public LogCat(int severity) { this(severity, false); }

    @Override
    protected void doLog(int severity, String tag, String rawMessage, Throwable throwable) {

        // Detail the log if needed
        final String detailedMessage = detailedLogs ? LogHelper.getDetailedLog(rawMessage) : rawMessage;

        // Add throwable trace
        final String message;
        if (throwable == null) {
            message = detailedMessage;
        } else {
            message = rawMessage + '\n' + LogHelper.getStackTrace(throwable);
        }

        switch (severity) {

            case Severity.VERBOSE:
                Log.println(Log.VERBOSE, tag, message);
                break;

            case Severity.DEBUG:
                Log.println(Log.DEBUG, tag, message);
                break;

            case Severity.INFO:
                Log.println(Log.INFO, tag, message);
                break;

            case Severity.WARN:
                Log.println(Log.WARN, tag, message);
                break;

            case Severity.ERROR:
                Log.println(Log.ERROR, tag, message);
                break;
        }
    }
}
