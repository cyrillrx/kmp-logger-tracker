package com.cyrillrx.android.logger.extension;

import com.crashlytics.android.Crashlytics;
import com.cyrillrx.android.logger.LogChild;
import com.cyrillrx.android.logger.LogWrapper;

/**
 * A ready-to-use severity-aware {@link LogChild} wrapping {@link Crashlytics} logger class.
 *
 * @author Cyril Leroux
 *         Created on 19/10/15
 */
public class CrashlyticsLogger extends LogWrapper {

    public CrashlyticsLogger(int severity) {
        super(severity, new WrappedLogger());
    }

    private static class WrappedLogger implements LogChild {
        @Override
        public void verbose(String tag, String message) {
            Crashlytics.log(message);
        }

        @Override
        public void verbose(String tag, String message, Throwable throwable) {
            Crashlytics.log(message);
            Crashlytics.logException(throwable);
        }

        @Override
        public void debug(String tag, String message) {
            Crashlytics.log(message);
        }

        @Override
        public void debug(String tag, String message, Throwable throwable) {
            Crashlytics.log(message);
            Crashlytics.logException(throwable);
        }

        @Override
        public void info(String tag, String message) {
            Crashlytics.log(message);
        }

        @Override
        public void info(String tag, String message, Throwable throwable) {
            Crashlytics.log(message);
            Crashlytics.logException(throwable);
        }

        @Override
        public void warning(String tag, String message) {
            Crashlytics.log(message);
        }

        @Override
        public void warning(String tag, String message, Throwable throwable) {
            Crashlytics.log(message);
            Crashlytics.logException(throwable);
        }

        @Override
        public void error(String tag, String message) {
            Crashlytics.log(message);
        }

        @Override
        public void error(String tag, String message, Throwable throwable) {
            Crashlytics.log(message);
            Crashlytics.logException(throwable);
        }
    }
}
