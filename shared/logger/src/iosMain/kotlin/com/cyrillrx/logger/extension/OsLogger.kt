package com.cyrillrx.logger.extension

import com.cyrillrx.logger.LogHelper
import com.cyrillrx.logger.Severity
import com.cyrillrx.logger.SeverityLogChild
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ptr
import platform.darwin.OS_LOG_DEFAULT
import platform.darwin.OS_LOG_TYPE_DEBUG
import platform.darwin.OS_LOG_TYPE_DEFAULT
import platform.darwin.OS_LOG_TYPE_ERROR
import platform.darwin.OS_LOG_TYPE_FAULT
import platform.darwin.OS_LOG_TYPE_INFO
import platform.darwin.__dso_handle
import platform.darwin._os_log_internal

class OsLogger(severity: Severity) : SeverityLogChild(severity) {
    override fun doLog(severity: Severity, tag: String, message: String, throwable: Throwable?) {
        val enhancedMessage = createMessageWithTrace(message, throwable)

        log(
            osLogSeverity = severity.toOsSeverity(),
            message = "${severity.emoji} $tag - $enhancedMessage}",
        )
    }

    // API found in Kermit library (https://github.com/touchlab/Kermit/blob/main/kermit-core/src/appleMain/kotlin/co/touchlab/kermit/OSLogWriter.kt)
    @OptIn(ExperimentalForeignApi::class)
    private fun log(osLogSeverity: UByte, message: String) {
        _os_log_internal(
            __dso_handle.ptr,
            OS_LOG_DEFAULT,
            osLogSeverity,
            "%s",
            message,
        )
    }

    companion object {
        private fun createMessageWithTrace(message: String, throwable: Throwable?): String {
            return if (throwable == null) {
                message
            } else {
                LogHelper.addClickableStackTrace(message, throwable)
            }
        }

        private fun Severity.toOsSeverity(): UByte = when (this) {
            Severity.INFO -> OS_LOG_TYPE_INFO
            Severity.WARN -> OS_LOG_TYPE_DEFAULT
            Severity.ERROR -> OS_LOG_TYPE_ERROR
            Severity.FATAL -> OS_LOG_TYPE_FAULT
            else -> OS_LOG_TYPE_DEBUG
        }
    }
}
