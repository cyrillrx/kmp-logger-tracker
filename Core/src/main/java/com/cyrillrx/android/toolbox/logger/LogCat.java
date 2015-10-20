package com.cyrillrx.android.toolbox.logger;

/**
 * @author Cyril Leroux
 *         Created on 18/10/2015.
 */
public class LogCat extends AbstractLogger {

    public LogCat(@LogSeverity int severity) { super(severity); }

    /**
     * Sends a {@link android.util.Log#VERBOSE} log message.
     *
     * @param tag     Used to identify the source of a log message. It usually identifies the class or activity where the log call occurs.
     * @param message The message you would like to log.
     */
    @Override
    protected void doLogVerbose(String tag, String message) {
        android.util.Log.v(tag, message);
    }

    /**
     * Sends a {@link android.util.Log#VERBOSE} log message.
     *
     * @param tag       Used to identify the source of a log message. It usually identifies the class or activity where the log call occurs.
     * @param message   The message you would like to log.
     * @param throwable An exception to log.
     */
    @Override
    protected void doLogVerbose(String tag, String message, Throwable throwable) {
        android.util.Log.v(tag, message, throwable);
    }

    /**
     * Sends a {@link android.util.Log#DEBUG} log message.
     *
     * @param tag     Used to identify the source of a log message. It usually identifies the class or activity where the log call occurs.
     * @param message The message you would like to log.
     */
    @Override
    protected void doLogDebug(String tag, String message) {
        android.util.Log.d(tag, message);
    }

    /**
     * Sends a {@link android.util.Log#DEBUG} log message.
     *
     * @param tag       Used to identify the source of a log message. It usually identifies the class or activity where the log call occurs.
     * @param message   The message you would like to log.
     * @param throwable An exception to log.
     */
    @Override
    protected void doLogDebug(String tag, String message, Throwable throwable) {
        android.util.Log.d(tag, message, throwable);
    }

    /**
     * Sends an {@link android.util.Log#INFO} log message.
     *
     * @param tag     Used to identify the source of a log message. It usually identifies the class or activity where the log call occurs.
     * @param message The message you would like to log.
     */
    @Override
    protected void doLogInfo(String tag, String message) {
        android.util.Log.i(tag, message);
    }

    /**
     * Sends an {@link android.util.Log#INFO} log message.
     *
     * @param tag       Used to identify the source of a log message. It usually identifies the class or activity where the log call occurs.
     * @param message   The message you would like to log.
     * @param throwable An exception to log.
     */
    @Override
    protected void doLogInfo(String tag, String message, Throwable throwable) {
        android.util.Log.i(tag, message, throwable);
    }

    /**
     * Sends a {@link android.util.Log#WARN} log message and log the exception.
     *
     * @param tag     Used to identify the source of a log message. It usually identifies the class or activity where the log call occurs.
     * @param message The message you would like to log.
     */
    @Override
    protected void doLogWarning(String tag, String message) {
        android.util.Log.w(tag, message);
    }

    /**
     * Sends a {@link android.util.Log#WARN} log message and log the exception.
     *
     * @param tag       Used to identify the source of a log message. It usually identifies the class or activity where the log call occurs.
     * @param message   The message you would like to log.
     * @param throwable An exception to log.
     */
    @Override
    protected void doLogWarning(String tag, String message, Throwable throwable) {
        android.util.Log.w(tag, message, throwable);
    }

    /**
     * Sends an {@link android.util.Log#ERROR} log message.
     *
     * @param tag     Used to identify the source of a log message. It usually identifies the class or activity where the log call occurs.
     * @param message The message you would like to log.
     */
    @Override
    protected void doLogError(String tag, String message) {
        android.util.Log.e(tag, message);
    }

    /**
     * Sends an {@link android.util.Log#ERROR} log message and log the exception.
     *
     * @param tag       Used to identify the source of a log message. It usually identifies the class or activity where the log call occurs.
     * @param message   The message you would like to log.
     * @param throwable An exception to log.
     */
    @Override
    protected void doLogError(String tag, String message, Throwable throwable) {
        android.util.Log.e(tag, message, throwable);
    }
}
