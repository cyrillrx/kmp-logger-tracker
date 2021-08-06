package com.cyrillrx.logger.extension

import com.cyrillrx.logger.L
import com.cyrillrx.logger.LogChild
import com.cyrillrx.logger.Severity

import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test

/**
 * @author Cyril Leroux
 *         Created on 03/05/2016.
 */
class SystemOutLogTest {

    @Test
    fun testLoggerVerbose() {
        testLogger(SystemOutLog(Severity.VERBOSE), "Testing verbose")
        testLogger(SystemOutLog(Severity.VERBOSE, true), "Testing verbose with source")
    }

    @Test
    fun testLoggerDebug() {
        testLogger(SystemOutLog(Severity.DEBUG), "Testing debug")
        testLogger(SystemOutLog(Severity.DEBUG, true), "Testing debug with source")
    }

    @Test
    fun testLoggerInfo() {
        testLogger(SystemOutLog(Severity.INFO), "Testing info")
        testLogger(SystemOutLog(Severity.INFO, true), "Testing info with source")
    }

    @Test
    fun testLoggerWarning() {
        testLogger(SystemOutLog(Severity.WARN), "Testing warning")
        testLogger(SystemOutLog(Severity.WARN, true), "Testing warning with source")
    }

    @Test
    fun testLoggerError() {
        testLogger(SystemOutLog(Severity.ERROR), "Testing error")
        testLogger(SystemOutLog(Severity.ERROR, true), "Testing error with source")
    }

    companion object {

        private val TAG = SystemOutLogTest::class.java.simpleName

        @JvmStatic
        @BeforeClass
        fun initLogger() {
            L.initialize()
        }

        @JvmStatic
        @AfterClass
        fun releaseLogger() {
            L.release()
        }

        private fun testLogger(child: LogChild, message: String) {

            L.addChild(child)

            val dummyException = Exception("This is an exception")

            logVerbose(message, dummyException)
            logDebug(message, dummyException)
            logInfo(message, dummyException)
            logWarning(message, dummyException)
            logError(message, dummyException)

            L.removeChild(child)
        }

        private fun logVerbose(message: String, e: Exception) {

            L.verbose(TAG, message)
            L.verbose(TAG, "$message with exception", e)
            logWithSeverity(Severity.VERBOSE, message, e)
        }

        private fun logDebug(message: String, e: Exception) {

            L.debug(TAG, message)
            L.debug(TAG, "$message with exception", e)
            logWithSeverity(Severity.DEBUG, message, e)
        }

        private fun logInfo(message: String, e: Exception) {

            L.info(TAG, message)
            L.info(TAG, "$message with exception", e)
            logWithSeverity(Severity.INFO, message, e)
        }

        private fun logWarning(message: String, e: Exception) {

            L.warning(TAG, message)
            L.warning(TAG, "$message with exception", e)
            logWithSeverity(Severity.WARN, message, e)
        }

        private fun logError(message: String, e: Exception) {

            L.error(TAG, message)
            L.error(TAG, "$message with exception", e)
            logWithSeverity(Severity.ERROR, message, e)
        }

        private fun logWithSeverity(severity: Int, message: String, e: Exception) {
            L.log(severity, TAG, message)
            L.log(severity, TAG, "$message with exception", e)
        }
    }
}
