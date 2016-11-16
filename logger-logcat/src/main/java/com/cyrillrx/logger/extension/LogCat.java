package com.cyrillrx.logger.extension;

import android.util.Log;

import com.cyrillrx.logger.LogChild;
import com.cyrillrx.logger.LogWrapper;
import com.cyrillrx.logger.Severity;

/**
 * A ready-to-use severity-aware {@link LogChild} wrapping {@link Log} class.
 *
 * @author Cyril Leroux
 *         Created on 18/10/2015.
 */
@SuppressWarnings("unused")
public class LogCat extends LogWrapper {

    public LogCat(int severity) { super(severity, new LogKitten()); }

    /**
     * Log cat child
     */
    private static class LogKitten implements LogChild {

        /**
         * Sends a log message.
         *
         * @param tag Used to identify the source of a log message.
         *            It usually identifies the class or activity where the log call occurs.
         * @param msg The message you would like to log.
         * @param th  An exception to log.
         */
        @Override
        public void log(int severity, String tag, String msg, Throwable th) {

            switch (severity) {

                case Severity.VERBOSE:
                    Log.println(Log.VERBOSE, tag, msg + '\n' + Log.getStackTraceString(th));
                    break;

                case Severity.DEBUG:
                    Log.println(Log.DEBUG, tag, msg + '\n' + Log.getStackTraceString(th));
                    break;

                case Severity.INFO:
                    Log.println(Log.INFO, tag, msg + '\n' + Log.getStackTraceString(th));
                    break;

                case Severity.WARN:
                    Log.println(Log.WARN, tag, msg + '\n' + Log.getStackTraceString(th));
                    break;

                case Severity.ERROR:
                    Log.println(Log.ERROR, tag, msg + '\n' + Log.getStackTraceString(th));
                    break;
            }
        }

        /**
         * Sends a log message.
         *
         * @param tag Used to identify the source of a log message.
         *            It usually identifies the class or activity where the log call occurs.
         * @param msg The message you would like to log.
         */
        @Override
        public void log(int severity, String tag, String msg) {

            switch (severity) {

                case Severity.VERBOSE:
                    Log.println(Log.VERBOSE, tag, msg);
                    break;

                case Severity.DEBUG:
                    Log.println(Log.DEBUG, tag, msg);
                    break;

                case Severity.INFO:
                    Log.println(Log.INFO, tag, msg);
                    break;

                case Severity.WARN:
                    Log.println(Log.WARN, tag, msg);
                    break;

                case Severity.ERROR:
                    Log.println(Log.ERROR, tag, msg);
                    break;
            }
        }
    }
}
