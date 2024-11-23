package com.cyrillrx.logger.extension;

import com.cyrillrx.logger.LogChild;
import com.cyrillrx.logger.LogHelper;
import com.cyrillrx.logger.Severity;
import com.cyrillrx.logger.SeverityLogChild;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

/**
 * A ready-to-use severity-aware {@link LogChild} wrapping {@link FirebaseCrashlytics} logger class.
 *
 * @author Cyril Leroux
 *         Created on 19/10/15
 */
/** A ready-to-use severity-aware [LogChild] wrapping [Crashlytics] logger class. */
class CrashlyticsLogger(severity: Severity) : SeverityLogChild(severity) {

    override fun doLog(severity: Severity, tag: String, message: String, throwable: Throwable?) {
        val enhancedMessage = if (throwable == null) {
            message
        } else {
            LogHelper.addClickableStackTrace(message, throwable)
        }

        val formattedMessage = LogHelper.formatLogWithDate(severity, tag, enhancedMessage)
        FirebaseCrashlytics.getInstance().log(formattedMessage)

        if (throwable != null) {
            FirebaseCrashlytics.getInstance().recordException(throwable)
        }
    }
}
