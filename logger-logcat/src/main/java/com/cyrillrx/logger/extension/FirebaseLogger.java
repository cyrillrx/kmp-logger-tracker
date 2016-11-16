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
@SuppressWarnings("unused")
public class FirebaseLogger extends LogWrapper {

    public FirebaseLogger(int severity) { super(severity, new FirebaseLogger.WrappedLogger()); }

    private static class WrappedLogger implements LogChild {

        @Override
        public void log(int severity, String tag, String message, Throwable throwable) {
            FirebaseCrash.log(message);
            FirebaseCrash.report(throwable);
        }

        @Override
        public void log(int severity, String tag, String message) {
            FirebaseCrash.log(message);
        }
    }
}
