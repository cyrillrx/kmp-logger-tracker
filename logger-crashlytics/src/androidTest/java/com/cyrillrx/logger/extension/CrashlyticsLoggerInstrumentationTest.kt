package com.cyrillrx.logger.extension

import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.crashlytics.android.Crashlytics
import com.cyrillrx.logger.Severity
import io.fabric.sdk.android.Fabric
import org.junit.Test
import org.junit.runner.RunWith

/**
 * @author Cyril Leroux
 *         Created 15/01/2019
 */
@RunWith(AndroidJUnit4::class)
class CrashlyticsLoggerInstrumentationTest {

    @Test
    fun testLogger() {

        Fabric.with(InstrumentationRegistry.getContext(), Crashlytics())

        val logger = CrashlyticsLogger(Severity.DEBUG)
        logger.log(Severity.DEBUG, "TOTO", "Test message")
    }
}