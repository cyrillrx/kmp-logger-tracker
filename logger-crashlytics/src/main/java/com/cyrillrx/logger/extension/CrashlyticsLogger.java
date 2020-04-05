package com.cyrillrx.logger.extension;

import com.cyrillrx.logger.LogChild;
import com.cyrillrx.logger.LogHelper;
import com.cyrillrx.logger.Severity;
import com.cyrillrx.logger.SeverityLogChild;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * A ready-to-use severity-aware {@link LogChild} wrapping {@link FirebaseCrashlytics} logger class.
 *
 * @author Cyril Leroux
 *         Created on 19/10/15
 */
@SuppressWarnings("unused")
public class CrashlyticsLogger extends SeverityLogChild {

    /**
     * The ISO-like date-time formatter that formats or parses a date-time with
     * offset and zone, such as '2011-12-03T10:15:30+01:00[CET]'
     */
    static final String ISO_DATE_TIME = "yyyy-MM-dd'T'HH:mm:ssZZZZZ'['zzz']'";

    private final SimpleDateFormat dateFormatter;
    private final FirebaseCrashlytics crashlytics;

    public CrashlyticsLogger(int severity, String dateTimePattern) {
        super(severity);
        crashlytics = FirebaseCrashlytics.getInstance();
        dateFormatter = new SimpleDateFormat(dateTimePattern, Locale.getDefault());
    }

    /**
     * Builds the {@link CrashlyticsLogger} with {@link #ISO_DATE_TIME} as default date-time pattern.
     *
     * @param severity The log severity level.
     */
    public CrashlyticsLogger(int severity) { this(severity, ISO_DATE_TIME); }

    @Override
    protected void doLog(int severity, String tag, String message, Throwable throwable) {

        if (throwable == null) {
            simpleLog(severity, tag, message);
            return;
        }

        final String stackTrace = LogHelper.getStackTrace(throwable);
        if (stackTrace == null) {
            simpleLog(severity, tag, message);
            return;
        }

        logWithStackTrace(severity, tag, message, stackTrace);
    }

    private void simpleLog(int severity, String tag, String message) {
        println("%s/%s : %s - %s", severityLabel(severity), tag, getCurrentDateTime(), message);
    }

    private void logWithStackTrace(int severity, String tag, String message, String stackTrace) {
        println("%s/%s : %s - %s\n%s", severityLabel(severity), tag, getCurrentDateTime(), message, stackTrace);
    }

    /** Formats data, prints into the "standard" output stream and then terminate the line. */
    private String getCurrentDateTime() { return dateFormatter.format(new Date()); }

    /**
     * Formats data, prints into the "standard" output stream and then terminate the line.
     */
    private void println(String format, Object... args) { println(String.format(format, args)); }

    /**
     * Prints a String into the "standard" output stream and then terminate the line.
     *
     * @param x The <code>String</code> to be printed.
     */
    private void println(String x) { crashlytics.log(x); }

    private static String severityLabel(int severity) {

        final String prefix;
        switch (severity) {
            case Severity.VERBOSE:
                return "V";

            case Severity.DEBUG:
                return "D";

            case Severity.INFO:
                return "I";

            case Severity.WARN:
                return "W";

            default:
                return "E";
        }
    }
}
