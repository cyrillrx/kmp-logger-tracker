package com.cyrillrx.logger.extension;

import com.cyrillrx.logger.LogChild;
import com.cyrillrx.logger.LogWrapper;
import com.google.firebase.crash.FirebaseCrash;

/**
 * A ready-to-use severity-aware {@link LogChild} wrapping {@link FirebaseCrash} logger class.
 *
 * @author Cyril Leroux
 *         Created 18/09/2016.
 */

public class FirebaseLogger extends LogWrapper {

    public FirebaseLogger(int severity) {
        super(severity, new FirebaseLogger.WrappedLogger());
    }

    private static class WrappedLogger implements LogChild {

        @Override
        public void verbose(String tag, String message) {
            FirebaseCrash.log(message);
        }

        @Override
        public void verbose(String tag, String message, Throwable throwable) {
            FirebaseCrash.log(message);
            FirebaseCrash.report(throwable);
        }

        @Override
        public void debug(String tag, String message) {
            FirebaseCrash.log(message);
        }

        @Override
        public void debug(String tag, String message, Throwable throwable) {
            FirebaseCrash.log(message);
            FirebaseCrash.report(throwable);
        }

        @Override
        public void info(String tag, String message) {
            FirebaseCrash.log(message);
        }

        @Override
        public void info(String tag, String message, Throwable throwable) {
            FirebaseCrash.log(message);
            FirebaseCrash.report(throwable);
        }

        @Override
        public void warning(String tag, String message) {
            FirebaseCrash.log(message);
        }

        @Override
        public void warning(String tag, String message, Throwable throwable) {
            FirebaseCrash.log(message);
            FirebaseCrash.report(throwable);
        }

        @Override
        public void error(String tag, String message) {
            FirebaseCrash.log(message);
        }

        @Override
        public void error(String tag, String message, Throwable throwable) {
            FirebaseCrash.log(message);
            FirebaseCrash.report(throwable);
        }
    }
}
