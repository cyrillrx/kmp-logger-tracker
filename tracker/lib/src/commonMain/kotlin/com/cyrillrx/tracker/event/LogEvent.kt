package com.cyrillrx.tracker.event

import com.cyrillrx.logger.LogHelper
import com.cyrillrx.logger.Severity
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

/**
 * @author Cyril Leroux
 *      Created on 25/04/2016.
 */
class LogEvent(
    val tag: String,
    val severity: Severity,
    val message: String,
    val screen: String,
    val createdAt: Instant = Clock.System.now(),
) : TrackEvent(LogHelper.formatLogWithDate(severity, tag, message, createdAt))
