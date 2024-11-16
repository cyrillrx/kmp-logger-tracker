package com.cyrillrx.logger;

import android.os.Build;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

/**
 * @author Cyril Leroux
 * Created on 03/01/2019.
 */
public class LogHelper {

    private static final String LOGGER_CLASS_NAME = Logger.class.getName();

    /**
     * @return The ISO-like date-time formatter that formats or parses a date-time with
     * offset and zone, such as '2011-12-03T10:15:30+01:00[Europe/Paris]'
     */
    private static String getCurrentDateTime() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
        } else {
            return (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZ", Locale.ROOT)).format(new Date());
        }
    }

    public static String simpleLog(int severity, String tag, String message) {
        return String.format("%s - %s - %s - %s", getCurrentDateTime(), Severity.getLabel(severity), tag, message);
    }

    public static String logWithStackTrace(int severity, String tag, String message, String stackTrace) {
        return String.format("%s - %s - %s - %s\n%s", getCurrentDateTime(), Severity.getLabel(severity), tag, message, stackTrace);
    }

    public static String getDetailedLog(String message) {

        final Thread currentThread = Thread.currentThread();
        final StackTraceElement[] stackElements = currentThread.getStackTrace();

        final StringBuilder sb = new StringBuilder();
        sb.append(message);

        sb.append("\nDetails: ");

        boolean lastWasLoggerClass = false;

        StackTraceElement trace = null;
        for (StackTraceElement stackElement : stackElements) {
            trace = stackElement;

            final String className = trace.getClassName();
            final boolean isLoggerClass = className.startsWith(LOGGER_CLASS_NAME);
            if (lastWasLoggerClass && !isLoggerClass) {
                break;
            }
            lastWasLoggerClass = isLoggerClass;
        }

        sb.append(String.format("%s [thread: %s]", linkableMethod(trace), currentThread.getName()));
        return sb.toString();
    }

    public static String linkableMethod(StackTraceElement trace) {
        if (trace == null) {
            return "trace is null";
        }
        return String.format(
                Locale.ROOT,
                "%s.%s(%s:%d)",
                trace.getClassName(),
                trace.getMethodName(),
                trace.getFileName(),
                trace.getLineNumber());
    }

    public static String linkableLine(StackTraceElement trace) {
        if (trace == null) {
            return "trace is null";
        }
        return String.format(Locale.ROOT, "(%s:%d)", trace.getFileName(), trace.getLineNumber());
    }

    /**
     * @param t The {@link Throwable} from which to extract the stack trace.
     * @return The stack trace as a {@link String}.
     */
    public static String getStackTrace(Throwable t) {

        try (final StringWriter sw = new StringWriter();
             final PrintWriter pw = new PrintWriter(sw)) {

            t.printStackTrace(pw);
            return sw.toString();

        } catch (Exception e) {
            return null;
        }
    }
}
