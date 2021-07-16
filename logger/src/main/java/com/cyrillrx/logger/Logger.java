package com.cyrillrx.logger;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

/**
 * This class wraps instances of the {@link LogChild} interface.
 * It allows to customize the logging conditions.
 *
 * @author Cyril Leroux
 *         Created on 03/09/2012.
 */
@SuppressWarnings("unused")
public class Logger {

    private static final String ERROR_ALREADY_INITIALIZED = "initialize() has already been called.";
    private static final String ERROR_INITIALIZE_FIRST = "Call initialize() before using the Logger.";

    private static Logger instance;

    private final Set<LogChild> loggers;

    @Nullable
    private ExceptionCatcher catcher;

    protected Logger() { loggers = new HashSet<>(); }

    public static void initialize() {
        checkMultiInitialization();

        instance = new Logger();
    }

    public static void release() { instance = null; }

    public static synchronized ExceptionCatcher setCatcher(@NotNull ExceptionCatcher catcher) {
        checkInitialized();

        return instance.catcher = catcher;
    }

    public static synchronized void addChild(@NotNull LogChild child) {
        checkInitialized();

        instance.loggers.add(child);
    }

    public static synchronized void removeChild(@NotNull LogChild child) {
        checkInitialized();

        instance.loggers.remove(child);
    }

    public static synchronized void log(int severity, String tag, @NotNull String message, @NotNull Throwable throwable) {
        checkInitialized();

        for (LogChild logger : instance.loggers) {
            try {
                logger.log(severity, tag, message, throwable);
            } catch (Throwable t) {
                gracefulCatch(t);
            }
        }
    }

    public static synchronized void log(int severity, @NotNull String tag, @NotNull String message) {
        checkInitialized();

        for (LogChild logger : instance.loggers) {
            try {
                logger.log(severity, tag, message, null);
            } catch (Throwable t) {
                gracefulCatch(t);
            }
        }
    }

    private static void gracefulCatch(@NotNull Throwable t) {

        final ExceptionCatcher catcher = instance.catcher;
        if (catcher == null) { return; }

        try {
            catcher.catchException(t);
        } catch (Exception ignored) {
            // Prevent the catcher from throwing an exception
        }
    }

    public static synchronized void verbose(@NotNull String tag, @NotNull String message, @NotNull Throwable throwable) {
        log(Severity.VERBOSE, tag, message, throwable);
    }

    public static synchronized void verbose(@NotNull String tag, @NotNull String message) {
        log(Severity.VERBOSE, tag, message);
    }

    public static synchronized void debug(@NotNull String tag, @NotNull String message, @NotNull Throwable throwable) {
        log(Severity.DEBUG, tag, message, throwable);
    }

    public static synchronized void debug(@NotNull String tag, @NotNull String message) {
        log(Severity.DEBUG, tag, message);
    }

    public static synchronized void info(@NotNull String tag, @NotNull String message, @NotNull Throwable throwable) {
        log(Severity.INFO, tag, message, throwable);
    }

    public static synchronized void info(@NotNull String tag, @NotNull String message) {
        log(Severity.INFO, tag, message);
    }

    public static synchronized void warning(@NotNull String tag, @NotNull String message, @NotNull Throwable throwable) {
        log(Severity.WARN, tag, message, throwable);
    }

    public static synchronized void warning(@NotNull String tag, @NotNull String message) {
        log(Severity.WARN, tag, message);
    }

    public static synchronized void error(@NotNull String tag, @NotNull String message, @NotNull Throwable throwable) {
        log(Severity.ERROR, tag, message, throwable);
    }

    public static synchronized void error(@NotNull String tag, @NotNull String message) {
        log(Severity.ERROR, tag, message);
    }

    /**
     * Checks whether the component has been initialized.<br />
     * Throws if not.
     */
    private static void checkInitialized() {
        if (instance == null) {
            throw new IllegalStateException(ERROR_INITIALIZE_FIRST);
        }
    }

    /**
     * Prevents multiple initialization of the component.<br />
     * Throws if the component has already been initialized.
     */
    private static void checkMultiInitialization() {
        if (instance != null) {
            throw new IllegalStateException(ERROR_ALREADY_INITIALIZED);
        }
    }

}
