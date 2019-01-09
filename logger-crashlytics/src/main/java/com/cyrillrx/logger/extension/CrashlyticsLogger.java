package com.cyrillrx.logger.extension;

import com.crashlytics.android.Crashlytics;
import com.cyrillrx.logger.LogChild;
import com.cyrillrx.logger.LogHelper;
import com.cyrillrx.logger.Severity;
import com.cyrillrx.logger.SeverityLogChild;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * A ready-to-use severity-aware {@link LogChild} wrapping {@link Crashlytics} logger class.
 *
 * @author Cyril Leroux
 *         Created on 19/10/15
 */
@SuppressWarnings("unused")
public class CrashlyticsLogger extends SeverityLogChild {

    /**
     * The ISO-like date-time formatter that formats or parses a date-time with
     * offset and zone, such as '2011-12-03T10:15:30+01:00[Europe/Paris]'
     */
    private static final SimpleDateFormat ISO_DATE_TIME;

    static {
        ISO_DATE_TIME = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX'['zzzz']'", Locale.getDefault());
    }

    public CrashlyticsLogger(int severity) { super(severity); }

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
        Crashlytics.logException(throwable);
    }

    private static void simpleLog(int severity, String tag, String message) {
        println("%s - %s - %s - %s", getCurrentDateTime(), Severity.getLabel(severity), tag, message);
    }

    private static void logWithStackTrace(int severity, String tag, String message, String stackTrace) {
        println("%s - %s - %s - %s\n%s", getCurrentDateTime(), Severity.getLabel(severity), tag, message, stackTrace);
    }

    /**
     * Formats data, prints into the "standard" output stream and then terminate the line.
     */
    private static void println(String format, Object... args) { println(String.format(format, args)); }

    /**
     * Prints a String into the "standard" output stream and then terminate the line.
     *
     * @param x The <code>String</code> to be printed.
     */
    private static void println(String x) { Crashlytics.log(x); }

    /** Formats data, prints into the "standard" output stream and then terminate the line. */
    private static String getCurrentDateTime() { return ISO_DATE_TIME.format(new Date()); }
}
