package com.cyrillrx.logger;

/**
 * @author Cyril Leroux
 *         Created on 20/10/2015.
 */
public class Severity {

    public static final int FATAL = 0;
    public static final int ERROR = 1;
    public static final int WARN = 2;
    public static final int INFO = 3;
    public static final int DEBUG = 4;
    public static final int VERBOSE = 5;

    public static String getLabel(int severity) {

        switch (severity) {

            case FATAL:
                return "FATAL";

            case ERROR:
                return "ERROR";

            case WARN:
                return "WARN";

            case INFO:
                return "INFO";

            case DEBUG:
                return "DEBUG";

            default:
            case VERBOSE:
                return "VERBOSE";
        }
    }
}
