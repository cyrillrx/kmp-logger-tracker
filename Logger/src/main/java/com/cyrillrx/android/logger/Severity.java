package com.cyrillrx.android.logger;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Cyril Leroux
 *         Created on 20/10/15
 */
public class Severity {

    public static final int FATAL = 0;
    public static final int ERROR = 1;
    public static final int WARN = 2;
    public static final int INFO = 3;
    public static final int DEBUG = 4;
    public static final int VERBOSE = 5;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({FATAL, ERROR, WARN, INFO, DEBUG, VERBOSE})
    public @interface LogSeverity { }
}
