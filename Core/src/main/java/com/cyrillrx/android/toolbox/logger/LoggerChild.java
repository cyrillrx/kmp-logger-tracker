package com.cyrillrx.android.toolbox.logger;

/**
 * @author Cyril Leroux
 *         Created on 18/10/2015.
 */
public interface LoggerChild {

    /**
     * @param tag     Used to identify the source of a log message.
     *                It usually identifies the class or activity where the log call occurs.
     * @param message The message you would like to log.
     */
    void verbose(String tag, String message);

    /**
     * @param tag       Used to identify the source of a log message.
     *                  It usually identifies the class or activity where the log call occurs.
     * @param message   The message you would like to log.
     * @param throwable An exception to log.
     */
    void verbose(String tag, String message, Throwable throwable);

    /**
     * @param tag     Used to identify the source of a log message.
     *                It usually identifies the class or activity where the log call occurs.
     * @param message The message you would like to log.
     */
    void debug(String tag, String message);

    /**
     * @param tag       Used to identify the source of a log message.
     *                  It usually identifies the class or activity where the log call occurs.
     * @param message   The message you would like to log.
     * @param throwable An exception to log.
     */
    void debug(String tag, String message, Throwable throwable);

    /**
     * @param tag     Used to identify the source of a log message.
     *                It usually identifies the class or activity where the log call occurs.
     * @param message The message you would like to log.
     */
    void info(String tag, String message);

    /**
     * @param tag       Used to identify the source of a log message.
     *                  It usually identifies the class or activity where the log call occurs.
     * @param message   The message you would like to log.
     * @param throwable An exception to log.
     */
    void info(String tag, String message, Throwable throwable);

    /**
     * @param tag     Used to identify the source of a log message.
     *                It usually identifies the class or activity where the log call occurs.
     * @param message The message you would like to log.
     */
    void warning(String tag, String message);

    /**
     * @param tag       Used to identify the source of a log message.
     *                  It usually identifies the class or activity where the log call occurs.
     * @param message   The message you would like to log.
     * @param throwable An exception to log.
     */
    void warning(String tag, String message, Throwable throwable);

    /**
     * @param tag     Used to identify the source of a log message.
     *                It usually identifies the class or activity where the log call occurs.
     * @param message The message you would like to log.
     */
    void error(String tag, String message);

    /**
     * @param tag       Used to identify the source of a log message.
     *                  It usually identifies the class or activity where the log call occurs.
     * @param message   The message you would like to log.
     * @param throwable An exception to log.
     */
    void error(String tag, String message, Throwable throwable);
}
